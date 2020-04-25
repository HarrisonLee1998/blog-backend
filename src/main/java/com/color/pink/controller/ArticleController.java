package com.color.pink.controller;

import com.color.pink.annotation.ApiOperation;
import com.color.pink.pojo.Article;
import com.color.pink.service.ArticleService;
import com.color.pink.util.PageUtil;
import com.color.pink.util.ResponseUtil;
import com.github.pagehelper.PageInfo;
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
    public ResponseUtil selectAll(HttpServletRequest request, @Valid PageUtil pageUtil) throws Exception {
        var response = ResponseUtil.factory(HttpStatus.OK);
        if(request.getRequestURI().equals("/article")) {
            articleService.selectAll(pageUtil, true, true, true, false);
        }else if(request.getRequestURI().equals("/admin/article")) {
            articleService.selectAll(pageUtil, false, false, true, false);
        }else{
            articleService.selectAll(pageUtil, false, false, true, true);
        }
        response.put("pageHelper", pageUtil);
        return response;
    }

    @ApiOperation("根据ID查询文章")
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

    @ApiOperation("根据标签查询文章")
    @GetMapping(value = {"admin/article/tag/{tagTitle}/{pageNo}/{pageSize}",
            "article/tag/{tagTitle}/{pageNo}/{pageSize}"})
    public ResponseUtil getArticlesByTagTitle(HttpServletRequest request,
                                              @PathVariable String tagTitle,
                                              @PathVariable Integer pageNo,
                                              @PathVariable Integer pageSize) {
        var response = ResponseUtil.factory();
        PageInfo<Article> pageInfo;
        var isAdmin = request.getRequestURI().startsWith("/admin");
        pageInfo = articleService.getArticlesByTagTitle(isAdmin, tagTitle, pageNo, pageSize);
        if(Objects.isNull(pageInfo) || pageInfo.getTotal() == 0) {
            response.setStatus(HttpStatus.NOT_FOUND);
        } else {
            response.put("pageInfo", pageInfo);
        }
        return response;
    }

    @ApiOperation("根据归档查询文章")
    @GetMapping(value = {"admin/article/archive/{archiveTitle}/{pageNo}/{pageSize}",
            "article/archive/{archiveTitle}/{pageNo}/{pageSize}"})
    public ResponseUtil getArticlesByArchiveTitle(HttpServletRequest request,
                                              @PathVariable String archiveTitle,
                                              @PathVariable Integer pageNo,
                                              @PathVariable Integer pageSize) {
        var response = ResponseUtil.factory();
        PageInfo<Article> pageInfo;
        var isAdmin = request.getRequestURI().startsWith("/admin");
        pageInfo = articleService.getArticlesByArchiveTitle(isAdmin, archiveTitle, pageNo, pageSize);
        if((Objects.isNull(pageInfo) || pageInfo.getTotal() == 0) && !isAdmin) {
            response.setStatus(HttpStatus.NOT_FOUND);
        } else {
            response.put("pageInfo", pageInfo);
        }
        return response;
    }
}
