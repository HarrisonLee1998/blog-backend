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

    @ApiOperation("查询所有标签")
    @GetMapping(value = {"admin/tag", "tag"})
    public ResponseUtil selectAllTag(HttpServletRequest request) {
        var response = ResponseUtil.factory(HttpStatus.OK);
        var tags = tagService.selectAll(request.getRequestURI().startsWith("/admin"));
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
        var pageInfo = tagService.selectAllByPage(request.getRequestURI().startsWith("/admin"),
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
}
