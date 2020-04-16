package com.color.pink.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HarrisonLee
 * @date 2020/4/3 18:12
 */
@Configuration
public class RestClientConfig{

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("192.168.0.101", 9200, "http")
            )
        );
    }
}
