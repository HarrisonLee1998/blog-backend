package com.color.pink;

import com.color.pink.pojo.Article;
import com.color.pink.service.ArticleService;
import com.color.pink.service.ElasticSearchService;
import com.color.pink.util.PageHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;

/**
 * @author HarrisonLee
 * @date 2020/4/17 20:20
 */
@SpringBootTest
public class ESTest {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private ArticleService articleService;

    @Test
    void testUpdate() throws IOException {
        var article = new Article();
        article.setId("43G7Nxcj3dV3");
        article.setTitle("测试标题ES修改2");
        article.setMarkdown("### Hello Markdown! 代码测试ES修改");
        article.setHtml("<h3><a id=\"Hello_Markdown_0\"></a>Hello Markdown!</h3> <p>代码测试ES修改</p>");
        article.setStar(10);
        article.setIsOpen(1);
        article.setIsDelete(1);
        article.setIsReward(1);
        article.setArchiveTitle("后端开发");
        // 发布日期其实不需要修改
        article.setPostDate(LocalDateTime.parse("2020-04-17 02:01:20", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        article.setLastUpdateDate(LocalDateTime.now());
        article.setViewTimes(0);
        var set = new HashSet<String>();
        set.add("spring");
        set.add("mybatis");
        set.add("java");
        set.add("mysql");
        article.setTags(set);
        elasticSearchService.updateArticle(article);
    }

    @Test
    void testGetDoc() throws IOException {
        String id = "43G7Nxcj3dV3";
        var doc = elasticSearchService.getDocById(id);
        doc.forEach((key, value) -> System.out.println(key + " : " + value));
    }

    @Test
    void testBulkInsert() throws IOException {
        var articles = articleService.selectAll();
        var result = elasticSearchService.bulkInsert(articles);
        System.out.println(result?"成功":"失败");
    }


    @Test
    void testSearchDoc01() throws IOException {
        String id = "rjX4QbQ1k1gY";
        var list = elasticSearchService.getOpenDocById(id, true);
        System.out.println(list.size());
        for (Map<String, Object> map : list) {
            map.forEach((key, value) -> {
                System.out.println(key+" : "+value);
            });
            System.out.println("==========================");
        }
    }

    @Test
    void testGetDocs() throws Exception {
        var pageHelper = new PageHelper();
        for(var i = 1; i<5; ++i){
            pageHelper.setPageNo(i);
            pageHelper.setPageSize(4);
            elasticSearchService.getDocs(true, true, pageHelper);
            System.out.println(pageHelper);
        }

//        for (Object object: pageHelper.getList()) {
//            ((Map<String,Object>)object).forEach((key, value) -> {
//                System.out.println(key+" : "+value);
//            });
//            System.out.println("==========================");
//        }
    }
}
