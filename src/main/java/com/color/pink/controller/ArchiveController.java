package com.color.pink.controller;

import com.color.pink.annotation.ApiOperation;
import com.color.pink.pojo.Archive;
import com.color.pink.service.ArchiveService;
import com.color.pink.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("查询所有归档(不查询附属文章)")
    @GetMapping(value = {"archive"})
    public ResponseUtil selectAllForClient(){
        var archives = archiveService.selectAllForClient();
        Objects.requireNonNull(archives);
        var response = ResponseUtil.factory();
        response.put("archives", archives);
        return response;
    }
    @ApiOperation("Admin查询所有归档")
    @GetMapping(value = {"admin/archive"})
    public ResponseUtil selectAllForAdmin(){
        var list = archiveService.selectAllForAdmin();
        Objects.requireNonNull(list);
        return ResponseUtil.factory(HttpStatus.OK).put("archives", list);
    }

    @ApiOperation("根据名称查询归档")
    @GetMapping(value = {"admin/archive/{archiveTitle}", "archive/{archiveTitle}"})
    public ResponseUtil getArchiveByTitle(HttpServletRequest request, @PathVariable String archiveTitle){
        var response = ResponseUtil.factory();
        var archive = archiveService.getArchiveByTitle(request.getRequestURI().startsWith("/admin"), archiveTitle);
        if(Objects.isNull(archive)) {
            response.setStatus(HttpStatus.NOT_FOUND);
        } else {
            response.put("archive", archive);
        }
        return response;
    }
}
