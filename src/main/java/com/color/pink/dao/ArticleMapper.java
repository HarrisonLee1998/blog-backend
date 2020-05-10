package com.color.pink.dao;

import com.color.pink.pojo.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ArticleMapper {
    int deleteByPrimaryKey(String id);

    int insert(Article record);

    List<Article> selectAll();
    List<Article> selectByTag(@Param("isAdmin") Boolean isAdmin, @Param("tagId") String tagId);

    List<Article> getArticleForSEO();

    boolean updateByPrimaryKey(Article record);

    boolean partialUpdateArticle(@Param("id") String id, @Param("map") Map<String, Object>map);


    boolean postArticle(Article article);

    List<Article>selectArticleByArchiveTitle(@Param("isAdmin") Boolean isAdmin,
                                             @Param("archiveTitle") String archiveTitle);

    // 不能删
    int countArticleNumsByArchive(String archive);
    int countArticleNumsByArchive2(String archive);
    // Set<Article> selectArticleByArchive2(String archive);
}