package com.color.pink;

import com.color.pink.pojo.Article;
import com.color.pink.service.ArticleService;
import com.color.pink.service.ElasticSearchService;
import com.color.pink.util.PageUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

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
        String id = "g2C31qhA9769";
        var map = elasticSearchService.getDocById(id, false, true, true, false);
        if (Objects.nonNull(map)) {
            map.forEach((key, value) -> {
                System.out.println(key + " : " + value);
            });
        } else {
            System.out.println("结果为空");
        }
        System.out.println("==========================");
    }

    @Test
    void testGetDocs() throws Exception {
        var pageUtil = new PageUtil<Map<String, Object>>();
        pageUtil.setPageNo(1);
        pageUtil.setPageSize(5);
        elasticSearchService.getDocs(pageUtil,false, false, true, false);
        System.out.println(pageUtil);
    }

    @Test
    void testSearchDocs() throws IOException {
        var pageUtil = new PageUtil<Map<String, Object>>();
        pageUtil.setPageNo(1);
        pageUtil.setPageSize(5);
        elasticSearchService.searchDocs(pageUtil,"库 编译 原理" ,false, true,
                false, true);
        System.out.println(pageUtil);
    }

    @Test
    void testPartialUpdate() throws Exception {
        var id = "4fB7xxknAE0u";
        var fields = new String[]{"is_open", "is_reward", "is_delete", "star"};
        var flags = new Integer[]{0, 0, 1, 1000};
        var map = new HashMap<String, Object>();
        for(var i = 0; i < fields.length; ++i) {
            map.put(fields[i], flags[i]);
        }
        System.out.println(elasticSearchService.partialUpdate(id, map));
    }
}
