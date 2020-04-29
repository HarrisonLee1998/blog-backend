package com.color.pink.service;

import com.color.pink.dao.TagMapper;
import com.color.pink.pojo.Tag;
import com.color.pink.util.CamelUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public boolean updateTag(Tag tag){
       return tagMapper.updateTitle(tag);
    }

    public Integer deleteInValidTag(){
        return tagMapper.deleteInValidTag();
    }
    public List<Tag>selectAllTag(){
        var tags = tagMapper.selectAll(true);
        Objects.requireNonNull(tags);
        return tags;
    }

    public boolean testTagByTitle(String title) {
        return tagMapper.testTagByTitle(title) > 0;
    }

    public List<Tag> selectAll(Boolean isAdmin) {
        var tags = tagMapper.selectAll(isAdmin);
        Objects.requireNonNull(tags);

        if(!isAdmin) {
            tags = tags.stream().filter(t -> t.getArticleNums() > 0).collect(Collectors.toList());
        }
        tags = tags.stream().sorted((x,y) -> y.getArticleNums() - x.getArticleNums()).collect(Collectors.toList());
        return tags;
    }

    public PageInfo<Tag> selectAllByPage(Boolean isAdmin,
                                         Integer pageNo,
                                         Integer pageSize,
                                         String sortBy,
                                         String sortDesc) {
        //对字段名由驼峰命名转下划线
        PageHelper.startPage(pageNo, pageSize < 0 ? Integer.MAX_VALUE - 1 : pageSize, CamelUtils.convert(sortBy) +" "+sortDesc);
        var tags = tagMapper.selectAll(isAdmin);
        Objects.requireNonNull(tags);

        if(!isAdmin) {
            tags = tags.stream().filter(t -> t.getArticleNums() > 0).collect(Collectors.toList());
        }
        return new PageInfo<>(tags);
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
