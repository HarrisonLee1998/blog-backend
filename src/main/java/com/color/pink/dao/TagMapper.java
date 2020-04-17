package com.color.pink.dao;

import com.color.pink.pojo.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface TagMapper {
    int deleteByPrimaryKey(String id);

    int insert(Tag record);

    Tag selectByPrimaryKey(String id);

    Set<Tag> selectAll();

    int updateByPrimaryKey(Tag record);

    Set<String> selectAllTagTitle();

    boolean addTags(@Param("tags") Set<Tag>tags);
    // 不能删
    Set<String>selectTagsTitleByArticleId(String artId);
}