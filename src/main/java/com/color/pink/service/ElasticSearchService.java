package com.color.pink.service;

import com.color.pink.pojo.Article;
import com.color.pink.pojo.ESArticle;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    public boolean postArticle(Article article) throws IOException {
        var esa = new ESArticle(article);
        var request = new IndexRequest(INDEX_ARTICLE);
        request.id(article.getId());
        request.timeout(new TimeValue(2, TimeUnit.SECONDS));// 设置超时时间为2秒
        request.source(objectMapper.writeValueAsString(esa), XContentType.JSON);
        var response = client.index(request, RequestOptions.DEFAULT);
        return RestStatus.OK.equals(response.status());
    }
}
