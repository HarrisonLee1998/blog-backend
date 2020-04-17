package com.color.pink.dao;

import com.color.pink.pojo.Article;
import com.color.pink.pojo.Tag;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * @author HarrisonLee
 * @date 2020/4/16 22:06
 */
public interface BlogMapper {
    // 统计每个标签所属的文章数量， 两个版本
    int countArticleByTag(String tagTd);
    int countArticleByTag2(String tagTd);
    // 根据标签查文章所属的文章，两个版本
    List<Article>selectArticleByTag(String tagTd);
    List<Article>selectArticleByTag2(String tagTd);

    // 添加文章标签关系
    boolean addBlogs(@Param("artId") String artId, @Param("tagIds") Set<String>tagIds);

    Set<Tag>selectTagsByArticleId(String id);

    boolean deleteBlogs(@Param("artId") String artId, @Param("tagIds") Set<String>tagIds);
}
