package com.color.pink.dao;

import com.color.pink.pojo.Menu;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MenuMapper {
    int deleteByPrimaryKey(@Param("title") String title, @Param("link") String link);

    int insert(Menu record);

    Menu selectByPrimaryKey(@Param("title") String title, @Param("link") String link);

    List<Menu> selectAll();

    int updateByPrimaryKey(Menu record);
}