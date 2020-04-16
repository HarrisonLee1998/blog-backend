package com.color.pink.controller;

import com.color.pink.annotation.ApiOperation;
import com.color.pink.pojo.Article;
import com.color.pink.service.ArticleService;
import com.color.pink.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    @PostMapping("/admin/article")
    public ResponseUtil postArticle(@RequestBody @Valid Article article) throws Exception {
        boolean result = articleService.postArticle(article);
        var response = ResponseUtil.factory(HttpStatus.OK);
        if(!result){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
