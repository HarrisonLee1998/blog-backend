package com.color.pink.controller;

import com.color.pink.annotation.ApiOperation;
import com.color.pink.pojo.Archive;
import com.color.pink.service.ArchiveService;
import com.color.pink.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

/**
 * @author HarrisonLee
 * @date 2020/4/5 1:04
 */
@ApiOperation("归档相关操作")
@RestController
public class ArchiveController {

    @Autowired
    private ArchiveService archiveService;

    @ApiOperation("添加归档")
    @PostMapping("admin/archive")
    public ResponseUtil addArchive(@Valid @RequestBody Archive archive){
        archiveService.addArchive(archive);
        return ResponseUtil.factory(HttpStatus.OK);
    }
    @ApiOperation("查询所有归档")
    @GetMapping(value = {"admin/archive", "archive"})
    public ResponseUtil selectAll(HttpServletRequest request){
        var list = archiveService.selectAll(request.getRequestURI().startsWith("/admin"));
        Objects.requireNonNull(list);
        return ResponseUtil.factory(HttpStatus.OK).put("archives", list);
    }
}
