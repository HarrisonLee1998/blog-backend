package com.color.pink.controller;

import com.color.pink.annotation.ApiOperation;
import com.color.pink.service.TagService;
import com.color.pink.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
}
