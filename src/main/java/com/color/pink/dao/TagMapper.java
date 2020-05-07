package com.color.pink.dao;

import com.color.pink.pojo.Tag;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TagMapper {
    boolean updateTitle(Tag tag);
    int deleteInValidTag();

    List<Tag> selectAll(Boolean isAdmin);

    @MapKey("title")
    Map<String, Tag>selectAllIdAndTitle();

    int testTagForClient(String title);
    int testTagForAdmin(String title);

    Set<String> selectAllTagTitle();

    boolean addTags(@Param("tags") Set<Tag>tags);

    int selectTagCount();

    // 不能删
    Set<String>selectTagsTitleByArticleId(String artId);
}