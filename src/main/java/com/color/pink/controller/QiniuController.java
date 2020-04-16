package com.color.pink.controller;

import com.color.pink.annotation.ApiOperation;
import com.color.pink.service.QiniuService;
import com.color.pink.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;


@ApiOperation("七牛相关")
@RestController
public class QiniuController {

    @Autowired
    private QiniuService qiniuService;

    @ApiOperation("获取七牛上传文件的凭证")
    @GetMapping("/admin/qiniu")
    public ResponseUtil getQiniuToken(){
        var token = qiniuService.getQiniuToken();
        Objects.requireNonNull(token);
        return ResponseUtil.factory(HttpStatus.OK).put("token", token);
    }

    @ApiOperation("删除七牛空间中的文件")
    @DeleteMapping("/admin/qiniu")
    public Map<String,Object>delFromQiniu(@RequestBody Map<String,Object>params) throws IOException {

        return null;
    }
}
