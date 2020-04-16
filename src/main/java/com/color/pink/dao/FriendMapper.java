package com.color.pink.dao;

import com.color.pink.pojo.Friend;
import java.util.List;

public interface FriendMapper {
    int deleteByPrimaryKey(String id);

    int insert(Friend record);

    Friend selectByPrimaryKey(String id);

    List<Friend> selectAll();

    int updateByPrimaryKey(Friend record);
}