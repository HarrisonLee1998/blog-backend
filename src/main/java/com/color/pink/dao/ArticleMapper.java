package com.color.pink.dao;

import com.color.pink.pojo.Article;

import java.util.List;

public interface ArticleMapper {
    int deleteByPrimaryKey(String id);

    int insert(Article record);

    List<Article> selectAll();

    int updateByPrimaryKey(Article record);

    boolean postArticle(Article article);

    // 下面两个提供给查询归档时，查询归档文章数量时用的
    int countArticleByArchive(String archive);
    int countArticleByArchive2(String archive);
}