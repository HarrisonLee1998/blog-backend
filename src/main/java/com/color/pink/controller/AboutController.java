package com.color.pink.controller;

import com.color.pink.annotation.ApiOperation;
import com.color.pink.service.AboutService;
import com.color.pink.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

/**
 * @author HarrisonLee
 * @date 2020/5/8 10:27
 */
@RestController
public class AboutController {

    @Autowired
    private AboutService aboutService;

    @ApiOperation("查询关于内容(markdown)")
    @GetMapping("admin/about/markdown")
    public ResponseUtil getMarkdown() {
        final var response = ResponseUtil.factory();
        final var markdown = aboutService.getMarkdown();
        if(Objects.isNull(markdown)) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            response.put("markdown", markdown);
        }
        return response;
    }

    @ApiOperation("查询关于内容(html)")
    @GetMapping(value = {"about", "admin/about/html"})
    public ResponseUtil getHtml() {
        final var response = ResponseUtil.factory();
        final var html = aboutService.getHtml();
        if(Objects.isNull(html)) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            response.put("html", html);
        }
        return response;
    }

    @ApiOperation("保存关于内容")
    @PostMapping("/admin/about")
    public ResponseUtil saveContent(@RequestBody Map<String, String> map) {
        final var response = ResponseUtil.factory();
        final var markdown = map.get("markdown");
        final var html = map.get("html");
        final var b = aboutService.saveContent(markdown, html);
        if(!b) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
