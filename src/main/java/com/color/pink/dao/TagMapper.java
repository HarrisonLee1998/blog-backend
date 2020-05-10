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

    // 包括id, title, articleNums
    List<Tag> selectAll(Boolean isAdmin);

    List<Tag> selectAllForAdmin();
    List<Tag> selectAllForClient();

    Tag selectByTitle(String title);

    @MapKey("title")
    Map<String, Tag>selectAllIdAndTitle();

    int testTagForClient(String title);
    int testTagForAdmin(String title);

    boolean addTags(@Param("tags") Set<Tag>tags);

    int selectTagCount();

    // 不能删
    Set<String>selectTagsTitleByArticleId(String artId);
}