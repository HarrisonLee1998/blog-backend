package com.color.pink.dao;

import com.color.pink.pojo.Article;

import java.util.List;
import java.util.Set;

public interface ArticleMapper {
    int deleteByPrimaryKey(String id);

    int insert(Article record);

    List<Article> selectAll();

    boolean updateByPrimaryKey(Article record);

    boolean postArticle(Article article);

    // 下面两个提供给查询归档时，查询归档文章数量时用的
    Set<Article> selectArticleByArchive(String archive);
    Set<Article> selectArticleByArchive2(String archive);
}