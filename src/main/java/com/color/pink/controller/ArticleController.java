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
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author HarrisonLee
 * @date 2020/4/5 0:46
 */
@ApiOperation("文章相关操作")
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    private Map<String, Deque<LocalDateTime>>history = new HashMap<>();

    private final Integer SEARCH_INTERVAL = 10;

    private final Integer CLEAR_INTERVAL = 60;

    @ApiOperation("发布文章")
    @PostMapping("admin/article")
    public ResponseUtil postArticle(@RequestBody @Valid Article article) throws Exception {
        var id = articleService.postArticle(article);
        var response = ResponseUtil.factory(HttpStatus.OK);
        if(Objects.isNull(id)){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("id", id);
        return response;
    }

    @ApiOperation("删除文章")
    @DeleteMapping("admin/article")
    public ResponseUtil deleteArticle(@RequestParam String id) throws InterruptedException {
        var response = ResponseUtil.factory();
        articleService.deleteArticle(id);
        return response;
    }

    @ApiOperation("修改文章")
    @PutMapping("admin/article")
    public ResponseUtil updateArticle(@RequestBody @Valid Article article) throws Exception {
        var result = articleService.updateArticle(article);
        var response = ResponseUtil.factory();
        if(!result) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @ApiOperation("更新部分文章信息")
    @PatchMapping(value = {"admin/article", "article"})
    public ResponseUtil partialUpdateArticle(@RequestBody Map<String, Object>map) throws InterruptedException {
        var response = ResponseUtil.factory();
        String id = (String) map.get("id");
        Objects.requireNonNull(id);
        map.remove("id");
        articleService.partialUpdateArticle(id, map);
        return response;
    }


    @ApiOperation("分页查询文章（请求ES）")
    @GetMapping(value = {"admin/article/{pageNo:^[1-9]\\d*$}/{pageSize:^-?[0-9]+$}",
            "article/{pageNo:^[1-9]\\d*$}/{pageSize:^-?[0-9]+$}",
            "admin/article/recycle/{pageNo:^[1-9]\\d*$}/{pageSize:^-?[0-9]+$}"})
    public ResponseUtil selectAll(HttpServletRequest request,
                                  @PathVariable Integer pageNo,
                                  @PathVariable Integer pageSize) throws Exception {
        if(pageSize < 0) {
            pageSize = Integer.MAX_VALUE - 1;
        }
        var pageUtil = new PageUtil<Map<String, Object>>();
        pageUtil.setPageNo(pageNo);
        pageUtil.setPageSize(pageSize);
        var response = ResponseUtil.factory(HttpStatus.OK);
        if(request.getRequestURI().startsWith("/article")) {
            articleService.selectAll(pageUtil, true, true, true, false);
        }else if(request.getRequestURI().startsWith("/admin/article/recycle")) {
            articleService.selectAll(pageUtil, false, false, true, true);
        }else{
            articleService.selectAll(pageUtil, false, false, true, false);
        }
        response.put("pageUtil", pageUtil);
        return response;
    }


    @ApiOperation("根据关键词搜索文章（请求ES）")
    @GetMapping(value = {"admin/article/search",
            "article/search",
            "admin/article/recycle/search"})
    public ResponseUtil searchDocs(HttpServletRequest request,
                                   @Valid PageUtil<Map<String, Object>>pageUtil,
                                   @NotBlank String keyword) throws Exception {
        if(pageUtil.getPageSize() < 0) {
            pageUtil.setPageSize(Integer.MAX_VALUE - 1);
        }
        var response = ResponseUtil.factory(HttpStatus.OK);
        if(request.getRequestURI().startsWith("/article")) {
            String ip = request.getRemoteAddr();
            var b = checkSearch(ip);
            if(b) {
                articleService.searchDocs(pageUtil, keyword, true, true, true, false);
            } else {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS);
            }
        }else if(request.getRequestURI().startsWith("/admin/article/recycle")) {
            articleService.searchDocs(pageUtil, keyword, false, false, true, true);
        }else{
            articleService.searchDocs(pageUtil, keyword,false, false, true, false);
        }
        response.put("pageUtil", pageUtil);
        return response;
    }

    public boolean checkSearch(String ip) {
        var queue = history.get(ip);
        if(Objects.isNull(queue)) {
            queue =  new ArrayDeque<>();
            queue.addFirst(LocalDateTime.now());
            history.put(ip, queue);
        }
        if(queue.size() > 2) {
            var lastDate = queue.getFirst();
            var duration = Duration.between(LocalDateTime.now(), lastDate);
            if(duration.toMinutes() < SEARCH_INTERVAL) {
                return false;
            } else if(duration.toMinutes() > CLEAR_INTERVAL) {
                queue.clear();
            }
        } else {
            queue.addFirst(LocalDateTime.now());
        }
        return true;
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
//        if((Objects.isNull(pageInfo) || pageInfo.getTotal() == 0) && !isAdmin) {
////            response.setStatus(HttpStatus.NOT_FOUND);
////        } else {
////            response.put("pageInfo", pageInfo);
////        }
        response.put("pageInfo", pageInfo);
        return response;
    }

    @ApiOperation("seo查询文章")
    @GetMapping("article/seo")
    public ResponseUtil getArticleForSEO(){
        var response = new ResponseUtil();
        var articles = articleService.getArticleForSEO();
        response.put("articles", articles);
        return response;
    }
}
