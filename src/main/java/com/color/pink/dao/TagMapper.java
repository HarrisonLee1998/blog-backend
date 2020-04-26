package com.color.pink.dao;

import com.color.pink.pojo.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface TagMapper {
    int deleteByPrimaryKey(String id);

    int insert(Tag record);

    Tag selectByPrimaryKey(String id);

    boolean updateTitle(Tag tag);
    Integer deleteInValidTag();

    List<Tag> selectAll(Boolean isAdmin);

    int updateByPrimaryKey(Tag record);

    Set<String> selectAllTagTitle();

    boolean addTags(@Param("tags") Set<Tag>tags);

    // 不能删
    Set<String>selectTagsTitleByArticleId(String artId);
}