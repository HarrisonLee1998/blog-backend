package com.color.pink.service;

import com.color.pink.pojo.Article;
import com.color.pink.pojo.ESArticle;
import com.color.pink.util.PageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author HarrisonLee
 * @date 2020/4/16 12:23
 */
@Service
public class ElasticSearchService {
    private final String INDEX_ARTICLE = "article";
    private final String INDEX_ARCHIVE = "archive";
    private final String INDEX_TAG = "tag";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    /**
     * 添加文档 测试通过
     * @param article
     * @return
     * @throws IOException
     */
    public boolean postArticle(Article article) throws IOException {
        var esa = new ESArticle(article);
        var request = new IndexRequest(INDEX_ARTICLE);
        request.id(article.getId());
        request.timeout(new TimeValue(2, TimeUnit.SECONDS));// 设置超时时间为2秒
        request.source(objectMapper.writeValueAsString(esa), XContentType.JSON);
        var response = client.index(request, RequestOptions.DEFAULT);
        return RestStatus.OK.equals(response.status());
    }

    /**
     * 更新文档 测试通过
     * 参考链接：
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-document-update.html
     * @param article
     * @return
     * @throws IOException
     */
    public boolean updateArticle(Article article) throws IOException {
        var esa = new ESArticle(article);
        var request = new UpdateRequest(INDEX_ARTICLE, article.getId());
        request.doc(objectMapper.writeValueAsString(esa), XContentType.JSON);
        request.timeout(new TimeValue(2, TimeUnit.SECONDS));
        // 要求更新后立即刷新
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        var response = client.update(request, RequestOptions.DEFAULT);
        return response.getResult() == DocWriteResponse.Result.UPDATED;
    }


    public boolean updateArticleStar(String id) throws Exception {
        Objects.requireNonNull(id);
        if(id.length() != 12) {
            throw new Exception("无效ID");
        }
        return false;
    }

    /**
     * 根据ID查询文章，无条件限制 测试通过
     * @param id
     * @return
     * @throws IOException
     */
    public Map<String, Object> getDocById(String  id) throws IOException {
        var request = new GetRequest(INDEX_ARTICLE, id);
        String[] includes = Strings.EMPTY_ARRAY;
        String[] excludes = {"is_delete", "is_open", "pure_txt"};
        var fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        request.fetchSourceContext(fetchSourceContext);
        var response = client.get(request, RequestOptions.DEFAULT);
        var map = response.getSourceAsMap();
        return map;
    }

    /**
     * 根据ID来查询文档，有响应的条件限制，测试通过
     * @param id
     * @param isOpen
     * @return
     * @throws IOException
     */
    public Map<String, Object> getDocById(String  id, Boolean filterOpen, boolean isOpen,
                                                boolean filterDelete, boolean isDelete) throws IOException {
        var request = new SearchRequest(INDEX_ARTICLE);
        String[] includes = Strings.EMPTY_ARRAY;
        var list = new ArrayList<String>();
        list.add("pure_txt");
        // 必须把不是公开的过滤掉，说明是客户端的请求，则将不需要的字段过滤掉
        if(filterOpen&&isOpen) {
            list.add("is_open");
            list.add("is_delete");
            list.add("markdown");
        }
        String[] excludes = new String[list.size()];
        list.toArray(excludes);
        var fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        var searchSourceBuilder = new SearchSourceBuilder();
        var queryBuilder = QueryBuilders.boolQuery();
        if(filterOpen) {
            var isOpenQuery = QueryBuilders.termQuery("is_open", isOpen ? 1 : 0);
            queryBuilder.must(isOpenQuery);
        }
        if(filterDelete) {
            var isDeleteQuery = QueryBuilders.termQuery("is_delete", isDelete ? 1 : 0);
            queryBuilder.must(isDeleteQuery);
        }

        // 必须满足ID相等
        var idQuery = QueryBuilders.termQuery("id", id);
        queryBuilder.must(idQuery);
        // 必须满足
        searchSourceBuilder.query(queryBuilder);

        //指定查询哪些字段
        searchSourceBuilder.fetchSource(fetchSourceContext);
        request.source(searchSourceBuilder);

        searchSourceBuilder.size(1);

        var response = client.search(request, RequestOptions.DEFAULT);
        if(response.getHits().getTotalHits().value > 0) {
            return response.getHits().getAt(0).getSourceAsMap();
        } else {
            return null;
        }
    }

    public PageUtil getDocs(PageUtil pageUtil, boolean filterOpen, boolean isOpen, boolean filterDelete, boolean isDelete) throws Exception {
        Objects.requireNonNull(pageUtil);
        pageUtil.check();
        var request = new SearchRequest(INDEX_ARTICLE);
        var searchSourceBuilder = new SearchSourceBuilder();
        var matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(matchAllQueryBuilder);
        var queryBuilder = QueryBuilders.boolQuery();
        // 是否过滤是否公开的
        if(filterOpen) {
            var isOpenQuery = QueryBuilders.termQuery("is_open", isOpen ? 1 : 0);
            queryBuilder.must(isOpenQuery);
        }
        // 是否过滤是否删除的
        if(filterDelete) {
            var isDeleteQuery = QueryBuilders.termQuery("is_delete", isDelete ? 1 : 0);
           queryBuilder.must(isDeleteQuery);
        }
        searchSourceBuilder.query(queryBuilder);
        /**
         * 指定查询字段
         * 这里的规则是: 集合includes-excludes，也就是在集合includes中，而不在excludes中的字段
         */
        String[] includes = Strings.EMPTY_ARRAY;
        var list = new ArrayList<String>();
        list.add("pure_txt");
        // 必须把不是公开的过滤掉，说明是客户端的请求，则将不需要的字段过滤掉
        if(filterOpen&&isOpen) {
            list.add("is_open");
            list.add("is_delete");
            list.add("markdown");
        }
        String[] excludes = new String[list.size()];
        list.toArray(excludes);
        var fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        searchSourceBuilder.fetchSource(fetchSourceContext);

        /**
         * 指定按照最后修改日期来排序，顺序为降序
         */
        var fieldSortBuilder = new FieldSortBuilder("last_update_date").order(SortOrder.DESC);
        searchSourceBuilder.sort(fieldSortBuilder);

        /**
         * 设置分页查询
         */
        searchSourceBuilder.from((pageUtil.getPageNo() - 1)* pageUtil.getPageSize());
        searchSourceBuilder.size(pageUtil.getPageSize());

        request.source(searchSourceBuilder);
        var response = client.search(request, RequestOptions.DEFAULT);
        return handleSearchResponse(response, pageUtil);
    }

    public PageUtil handleSearchResponse(SearchResponse response, PageUtil pageUtil){
        pageUtil.setTotal((int) response.getHits().getTotalHits().value);

        var list = new ArrayList<Map<String, Object>>();
//        response.getHits().forEach(hit -> {
//            list.add(hit.getSourceAsMap());
//        });
//        pageHelper.setList(Collections.singletonList(list));

        var length = response.getHits().getHits().length;
        for(int i = 0; i < length; ++i){
            list.add(response.getHits().getHits()[i].getSourceAsMap());
        }

        pageUtil.setList(Collections.singletonList(list));

        int p = pageUtil.getTotal() / pageUtil.getPageSize();
        if(pageUtil.getTotal() % pageUtil.getPageSize() != 0) {
            p += 1;
        }
        pageUtil.setPages(p);

        /**
         * 如果有数据，直接根据pageNo == 1即可判断是否是第一页
         * 如果没有数据，则需要再判断当前页码小于等于总页数
         */
        if(pageUtil.getPageNo() == 1 && pageUtil.getPageNo() <= pageUtil.getPages()) {
            pageUtil.setFirst(true);
        } else {
            pageUtil.setFirst(false);
        }

        /**
         * 直接判断当前页码是否等于总页数即可，因为如果数据为空的话，那么pages就为0了
         */
        if(pageUtil.getPageNo() == pageUtil.getPages()) {
            pageUtil.setLast(true);
        } else {
            pageUtil.setLast(false);
        }


        /**
         * 如何判断是否有上一页或者下一页
         *
         * 判断前一页的起点是否在总数范围内
         * (pageHelper.getPageSize() * (pageHelper.getPageNo() - 2) + 1)
         *  正是前一页第一个元素的下标（从1开始）
         */
        if(pageUtil.getPageNo() > 1 &&
                (pageUtil.getPageSize() * (pageUtil.getPageNo() - 2) + 1) <= pageUtil.getTotal()) {
            pageUtil.setHasPrevious(true);
        } else {
            pageUtil.setHasPrevious(false);
        }

        if(((pageUtil.getPageNo()) * pageUtil.getPageSize() + 1) <= pageUtil.getTotal()) {
            pageUtil.setHashNext(true);
        } else {
            pageUtil.setHashNext(false);
        }
//
//        if(!pageHelper.isHasPrevious() &&
//                ((pageHelper.getPageNo() - 1) * pageHelper.getPageSize() + 1) <= pageHelper.getTotal()) {
//            pageHelper.setFirst(true);
//        } else {
//            pageHelper.setFirst(false);
//        }
//
//        if(!pageHelper.isHashNext() &&
//                ((pageHelper.getPageNo() - 1) * pageHelper.getPageSize() + 1) < pageHelper.getTotal()) {
//            pageHelper.setLast(true);
//        } else {
//            pageHelper.setLast(false);
//        }
        return pageUtil;
    }


    /**
     * 批量插入，测试通过
     * @param list
     * @return
     * @throws IOException
     */
    public boolean bulkInsert(List<Article>list) throws IOException {
        var esArticles = new ArrayList<ESArticle>();
        for (Article article : list) {
            esArticles.add(new ESArticle(article));
        }
        var request = new BulkRequest(INDEX_ARTICLE);
        esArticles.forEach(e -> {
            try {
                request.add(
                        new IndexRequest().id(e.getId())
                        .source(objectMapper.writeValueAsString(e), XContentType.JSON)
                );
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
                System.out.println("对象序列化失败");
            }
        });
        var response = client.bulk(request, RequestOptions.DEFAULT);
        return RestStatus.OK.equals(response.status());
    }
}
