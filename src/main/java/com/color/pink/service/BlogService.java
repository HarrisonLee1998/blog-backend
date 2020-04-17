package com.color.pink.service;

import com.color.pink.dao.BlogMapper;
import com.color.pink.pojo.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

/**
 * @author HarrisonLee
 * @date 2020/4/16 23:14
 */
@Service
public class BlogService {
    @Autowired
    private BlogMapper blogMapper;

    public boolean addBlogs(String artId, Set<String>tagIds) {
        return blogMapper.addBlogs(artId, tagIds);
    }

    public Set<Tag>selectTagsByArticleId(String id) {
        Set<Tag> tags = blogMapper.selectTagsByArticleId(id);
        Objects.requireNonNull(tags);
        return tags;
    }

    public boolean deleteBlogs(String artId, Set<String>tagIds) {
        return blogMapper.deleteBlogs(artId, tagIds);
    }
}
