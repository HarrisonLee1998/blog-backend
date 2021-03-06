package com.color.pink.controller;

import com.color.pink.annotation.ApiOperation;
import com.color.pink.pojo.Tag;
import com.color.pink.service.TagService;
import com.color.pink.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author HarrisonLee
 * @date 2020/4/5 1:04
 */
@RestController
public class TagController {

    @Autowired
    private TagService tagService;

    private final String PROJECT_PREFIX = "/blog";

    @ApiOperation("查询所有标签数量")
    @GetMapping("admin/tag/count")
    public ResponseUtil selectTagCount() {
        final var response = ResponseUtil.factory();
        final var count = tagService.selectTagCount();
        response.put("count", count);
        return response;
    }

    @ApiOperation("查询所有标签")
    @GetMapping(value = {"admin/tag", "tag"})
    public ResponseUtil selectAllTag(HttpServletRequest request) {
        var response = ResponseUtil.factory(HttpStatus.OK);
        var tags = tagService.selectAll(request.getRequestURI().startsWith(PROJECT_PREFIX + "/admin"));
        if (Objects.isNull(tags)) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("tags", tags);
        return response;
    }

    @ApiOperation("分页查询所有标签")
    @GetMapping(value = {"admin/tag/{pageNo:^[1-9]\\d*$}/{pageSize:^-?[0-9]+$}/{sortBy}/{sortDesc}",
            "tag/{pageNo:^[1-9]\\d*$}/{pageSize:^-?[0-9]+$}/{sortBy}/{sortDesc}"})
    public ResponseUtil selectAllTagByPage(HttpServletRequest request,
                                           @PathVariable Integer pageNo,
                                           @PathVariable Integer pageSize,
                                           @PathVariable @NotBlank String sortBy,
                                           @PathVariable @NotBlank String sortDesc) {
        var response = ResponseUtil.factory(HttpStatus.OK);
        var sortBys = Arrays.asList("createDate", "viewTimes", "articleNums",
                "create_date", "view_times", "article_nums");
        var descs = Arrays.asList("DESC", "ASC", "desc", "asc");
        if( !sortBys.contains(sortBy) || !descs.contains(sortDesc)){
            response.setStatus(HttpStatus.BAD_REQUEST);
            return  response;
        }
        var pageInfo = tagService.selectAllByPage(request.getRequestURI().startsWith(PROJECT_PREFIX + "/admin"),
                pageNo, pageSize, sortBy, sortDesc);
        if (Objects.isNull(pageInfo)) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("pageInfo", pageInfo);
        return response;
    }

    @ApiOperation("修改标签名称")
    @PatchMapping("admin/tag")
    public ResponseUtil updateTag(@RequestBody Tag tag) {
        var result = tagService.updateTag(tag);
        var response = ResponseUtil.factory();
        if(result) {
            response.setStatus(HttpStatus.OK);
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @ApiOperation("删除无效标签")
    @DeleteMapping("admin/tag")
    public ResponseUtil deleteInValidTag(){
        var rows = tagService.deleteInValidTag();
        var response = ResponseUtil.factory();
        response.put("rows", rows);
        return response;
    }

    @ApiOperation("验证标签是否存在")
    @GetMapping(value = {"admin/tag/test/{title}", "tag/test/{title}"})
    public ResponseUtil testTagByTitle(HttpServletRequest request, @PathVariable String title) {
        var response = ResponseUtil.factory();
        var result = tagService.testTagByTitle(request.getRequestURI().startsWith(PROJECT_PREFIX + "/admin"), title);
        if(!result) {
            response.setStatus(HttpStatus.NOT_FOUND);
        }
        return response;
    }
}
