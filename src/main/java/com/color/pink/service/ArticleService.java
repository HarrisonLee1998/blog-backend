package com.color.pink.service;

import com.color.pink.dao.ArticleMapper;
import com.color.pink.pojo.Article;
import com.color.pink.pojo.Tag;
import com.color.pink.util.UUIDHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

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
            var tags = tagService.selectAllTag();
            map = new HashMap<>();
            tags.forEach(t -> map.put(t.getTitle(), t));
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
}
