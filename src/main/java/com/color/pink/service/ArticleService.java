package com.color.pink.service;

import com.color.pink.dao.ArticleMapper;
import com.color.pink.pojo.Article;
import com.color.pink.pojo.Tag;
import com.color.pink.util.PageUtil;
import com.color.pink.util.UUIDHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author HarrisonLee
 * @date 2020/4/16 12:12
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ElasticSearchService elasticSearchService;

    private Map<String, Tag>map;

    public Map<String, Object>getArticleById(String id, boolean filterOpen, boolean isOpen,
                                             boolean filterDelete, boolean isDelete) throws IOException {
        return elasticSearchService.getDocById(id, filterOpen, isOpen, filterDelete, isDelete);
    }

    public List<Article>selectAll() {
        var list = articleMapper.selectAll();
        Objects.requireNonNull(list);
        return list;
    }

    public void selectAll(PageUtil pageUtil, boolean filterOpen, boolean isOpen,
                          boolean filterDelete, boolean isDelete) throws Exception {
        elasticSearchService.getDocs(pageUtil, filterOpen, isOpen, filterDelete, isDelete);
        Objects.requireNonNull(pageUtil.getList());
    }

    public boolean postArticle(Article article) throws Exception {
        Integer ID_LEN = 12;
        article.setId(UUIDHelper.rand(ID_LEN));
        article.setPostDate(LocalDateTime.now());
        article.setLastUpdateDate(article.getPostDate());
        article.setViewTimes(0);
        article.setIsDelete(0);
        article.setStar(0);
        // 接下来分别交由ES和MyBatis处理
        var es = new Thread(()->{
            try {
                elasticSearchService.postArticle(article);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.start();
        // 保存数据到数据库
        // 先处理标签
        if(Objects.isNull(map)) {
            fillMap();
        }
        // 以下操作，map不能为空
        Objects.requireNonNull(map);

        // 不在数据库中的新标签
        var nTags = new HashSet<Tag>();
        // 文章标签的id
        var tagIds = new HashSet<String>();
        // 遍历文章的标签
        for(String s:article.getTags()){
            if(map.containsKey(s)) {
                tagIds.add(map.get(s).getId()); // 保存已存在的标签ID
            }else{
                var t = new Tag();
                t.setId(UUIDHelper.rand(12));
                t.setCreateDate(LocalDateTime.now());
                t.setTitle(s);
                t.setViewTimes(0);

                nTags.add(t);
                tagIds.add(t.getId()); // 保存未存在的标签ID
                map.put(s, t); // 更新本地标签集合数据
            }
        }

        boolean result;

        if(nTags.size() > 0){
            result = tagService.addTags(nTags);
            if (!result) {
                throw new Exception("保存文章的标签失败");
            }
        }

        // 存文章到数据库
        result = articleMapper.postArticle(article);
        if (!result) {
            throw new Exception("保存文章失败");
        }

        // 存标签-文章关系
        result = blogService.addBlogs(article.getId(), tagIds);
        if (!result) {
            throw new Exception("保存文章标签关系（博客）失败");
        }
        es.join();
        return true;
    }

    public boolean updateArticle(Article article) throws Exception {

        Objects.requireNonNull(article.getId());
        if(article.getId().length() != 12){
            throw new Exception("文章ID不正确");
        }

        // 新数据兵分两路，一路直接交给ES处理，另一路交给MySQL处理

        var es = new Thread(()->{
            try {
                elasticSearchService.updateArticle(article);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.start();

        // 先根据文章id查询数据库中的文章的旧标签集合

        /*
         * 旧标签集合为A，新标签集合为B，对于A与B的交集不做处理
         * 对于A-B，需要删除blog关系，记作C
         * 对于B-A，有两种情况，记作D
         *  1. 在数据库中存在，直接存blog关系即可
         *  2. 不在数据库中，需要先新建标签，然后再存blog关系
         *
         * 最后再更新文章
         */
        var oldTags = blogService.selectTagsByArticleId(article.getId()); //这是Tag Set
        var A = new HashSet<String>();// 这是字符串Set, 从oldTags中取出来的
        oldTags.forEach(t -> {
            A.add(t.getTitle());
        });
        var B = article.getTags(); // 这是字符串Set
        var C = new HashSet<>(A);
        C.removeAll(B);
        var D = new HashSet<>(B);
        D.removeAll(A);

        var tagIds = new HashSet<String>();

        // 删除C中标签与文章的关系
        if(Objects.isNull(map)) {
            fillMap();
        }
        for (String s : C) {
            tagIds.add(map.get(s).getId());
        }
        boolean result;
        if(tagIds.size() > 0) {
            result = blogService.deleteBlogs(article.getId(), tagIds);
            if (!result) {
                throw new Exception("删除旧标签-文章关系失败");
            }
        }
        tagIds.clear();
        // 新建D中的标签，并添加标签与文章的关系
        var nTags = new HashSet<Tag>();
        for (String s : D) {
            var t = new Tag();
            t.setId(UUIDHelper.rand(12));
            t.setTitle(s);
            t.setCreateDate(LocalDateTime.now());
            t.setViewTimes(0);
            t.setArticleNums(0);

            nTags.add(t);
            tagIds.add(t.getId());
            map.put(s, t);
        }

        if(nTags.size() > 0) {
            result = tagService.addTags(nTags);
            if (!result) {
                throw new Exception("删除旧标签-文章关系失败");
            }
        }

        result = blogService.addBlogs(article.getId(), tagIds);
        if (!result) {
            throw new Exception("删除旧标签-文章关系失败");
        }
        result = articleMapper.updateByPrimaryKey(article);
        es.join();
        return result;
    }

    public void fillMap(){
        var tags = tagService.selectAllTag();
        map = new HashMap<>();
        tags.forEach(t -> map.put(t.getTitle(), t));
    }

}

