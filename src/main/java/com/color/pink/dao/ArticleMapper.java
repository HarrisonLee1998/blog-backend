package com.color.pink.dao;

import com.color.pink.pojo.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ArticleMapper {
    int deleteByPrimaryKey(String id);

    int insert(Article record);

    List<Article> selectAll();
    List<Article> selectByTag(@Param("isAdmin") Boolean isAdmin, @Param("tagId") String tagId);

    boolean updateByPrimaryKey(Article record);

    boolean partialUpdateArticle(@Param("id") String id, @Param("map") Map<String, Object>map);


    boolean postArticle(Article article);

    List<Article>selectArticleByArchiveTitle(@Param("isAdmin") Boolean isAdmin,
                                             @Param("archiveTitle") String archiveTitle);
    // 下面两个提供给查询归档时，查询归档文章时用的
    Set<Article> selectArticleByArchive(String archive);
    Set<Article> selectArticleByArchive2(String archive);
}