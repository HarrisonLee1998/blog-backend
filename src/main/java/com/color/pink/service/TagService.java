package com.color.pink.service;

import com.color.pink.dao.TagMapper;
import com.color.pink.pojo.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author HarrisonLee
 * @date 2020/4/16 22:21
 */
@Service
public class TagService {

    @Autowired
    private TagMapper tagMapper;

    public Set<Tag>selectAllTag(){
        var tags = tagMapper.selectAll();
        Objects.requireNonNull(tags);
        return tags;
    }

    public Set<Tag>selectAll(Boolean isAdmin) {
        var tags = isAdmin ? tagMapper.selectAll() : tagMapper.selectAll2();
        Objects.requireNonNull(tags);

        if(!isAdmin) {
            tags = tags.stream().filter(t -> t.getArticleNums() > 0).collect(Collectors.toSet());
        }
        tags = tags.stream().sorted((x,y) -> y.getArticleNums() - x.getArticleNums()).collect(Collectors.toCollection(LinkedHashSet::new));
        return tags;
    }

    public Set<String>selectAllTagTitle(){
        var titles = tagMapper.selectAllTagTitle();
        Objects.requireNonNull(titles);
        return titles;
    }

    public boolean addTags(Set<Tag>tags){
        return tagMapper.addTags(tags);
    }
}
