package com.color.pink.controller;

import com.color.pink.annotation.ApiOperation;
import com.color.pink.pojo.Article;
import com.color.pink.service.ArticleService;
import com.color.pink.util.PageHelper;
import com.color.pink.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author HarrisonLee
 * @date 2020/4/5 0:46
 */
@ApiOperation("文章相关操作")
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation("发布文章")
    @PostMapping("admin/article")
    public ResponseUtil postArticle(@RequestBody @Valid Article article) throws Exception {
        boolean result = articleService.postArticle(article);
        var response = ResponseUtil.factory(HttpStatus.OK);
        if(!result){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @ApiOperation("获取文章")
    @GetMapping(value = {"admin/article", "article", "admin/article/recycle"})
    public ResponseUtil selectAll(HttpServletRequest request, @Valid PageHelper pageHelper) throws Exception {
        var response = ResponseUtil.factory(HttpStatus.OK);
        if(request.getRequestURI().equals("/article")) {
            articleService.selectAll(pageHelper, true, true, true, false);
        }else if(request.getRequestURI().equals("/admin/article")) {
            articleService.selectAll(pageHelper, false, false, true, false);
        }else{
            articleService.selectAll(pageHelper, false, false, true, true);
        }
        response.put("pageHelper", pageHelper);
        return response;
    }

    @GetMapping(value = {"admin/article/entry/{id}", "article/entry/{id}"})
    public ResponseUtil getArticleById(HttpServletRequest request, @PathVariable String id) throws IOException {
        var response = ResponseUtil.factory();
        Map<String, Object> article;
        if(request.getRequestURI().startsWith("/admin")) {
            // 对于管理端的访问，没有限制
            article = articleService.getArticleById(id, false, false, false, false);
        } else {
            // 对于客户端的访问，必须是公开且未删除的
            article = articleService.getArticleById(id, true, true, true, false);
        }
        response.put("article", article);
        if(Objects.isNull(article)) {
            response.setStatus(HttpStatus.NOT_FOUND);
        }
        return response;
    }
}
