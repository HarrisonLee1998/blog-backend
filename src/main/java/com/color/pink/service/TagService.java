package com.color.pink.service;

import com.color.pink.dao.TagMapper;
import com.color.pink.pojo.Tag;
import com.color.pink.util.CamelUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    public Integer selectTagCount() {
        return tagMapper.selectTagCount();
    }

    /**
     * 测试标签是否存在
     * @param title
     * @param isAdmin
     * @return
     */
    public boolean testTagByTitle(Boolean isAdmin, String title) {
        if(isAdmin) {
            return tagMapper.testTagForAdmin(title) > 0;
        } else {
            return tagMapper.testTagForClient(title) > 0;
        }
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

    /**
     * 此方法在发布文章时调用
     * 此方法不被外部Controller调用
     * @return
     */
    public Map<String, Tag> selectAllIdAndTitle(){
        return tagMapper.selectAllIdAndTitle();
    }

    /**
     * Admin分页查询标签，其实没有被调用
     * @param isAdmin
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDesc
     * @return
     */
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

    /**
     * 在发布文章时，可能需要添加标签
     * @param tags
     * @return
     */
    public boolean addTags(Set<Tag>tags){
        return tagMapper.addTags(tags);
    }
}
