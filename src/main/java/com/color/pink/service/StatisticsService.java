package com.color.pink.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author HarrisonLee
 * @date 2020/5/5 8:18
 */
@Service
public class StatisticsService {

    private static Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    @Value("${baidu.tongji.access.token}")
    private String accessToken;

    private static LocalDate refreshTokenDate = LocalDate.now();

    @Value("${baidu.tongji.code}")
    private String code;

    @Value("${baidu.tongji.refresh.token}")
    private String refreshToken;

    @Value("${baidu.tongji.client.id}")
    private String clientId;

    @Value("${baidu.tongji.client.secret}")
    private String clientSecret;

    private final String siteId = "14958271";

    private final String START_DATE = "20200501";

    private Map<String, String>data = new HashMap<>();

    private ObjectMapper objectMapper = new ObjectMapper();

    public String getProvincePVCount(){
        final var map = new LinkedHashMap<>();
        map.put("start_date", START_DATE);
        map.put("end_date", formatDate(LocalDate.now().minusDays(1)));
        map.put("method", "visit/district/a");
        final var isExisted = checkIsExisted("visit_district_a");
        if(isExisted) {
            return data.get("visit_district_a");
        } else {
            String s = getData(map);
            data.put("visit_district_a", s);
            return s;
        }
    }

    public String getWorldPVCount(){
        final var map = new LinkedHashMap<>();
        map.put("start_date", START_DATE);
        map.put("end_date", formatDate(LocalDate.now().minusDays(1)));
        map.put("method", "visit/world/a");
        final var isExisted = checkIsExisted("visit_world_a");
        if(isExisted) {
            return data.get("visit_world_a");
        } else {
            String s = getData(map);
            data.put("visit_world_a", s);
            return s;
        }
    }

    public String getUVTrend(int days){
        final var map = new LinkedHashMap<>();
        map.put("start_date", formatDate(LocalDate.now().minusDays(days)));
        map.put("end_date", formatDate(LocalDate.now().minusDays(1)));
        map.put("method", "overview/getTimeTrendRpt");
        map.put("metrics", "visitor_count");
        final var isExisted = checkIsExisted("visitor_count");
        if(isExisted) {
            return data.get("visitor_count");
        } else {
            String s = getData(map);
            data.put("visitor_count", s);
            return s;
        }
    }

    public String getPVTrend(int days) {
        final var map = new LinkedHashMap<>();
        map.put("start_date", formatDate(LocalDate.now().minusDays(days)));
        map.put("end_date", formatDate(LocalDate.now().minusDays(1)));
        map.put("method", "overview/getTimeTrendRpt");
        map.put("metrics", "pv_count");
        final var isExisted = checkIsExisted("pv_count");
        if(isExisted) {
            return data.get("pv_count");
        } else {
            String s = getData(map);
            data.put("pv_count", s);
            return s;
        }
    }

    public String getData(LinkedHashMap<Object, Object>map){
        refreshToken();
        // String START_DATE = "20200501";
        // String END_DATE = formatDate(LocalDate.now());
        // String METRICS = "pv_count";
        /**
         * 注意一下，这里有一个坑，从百度复制的URL中，下面的overview后面是 '%2f'字样
         * 如果下面字符串直接保存 '%2f'， encode后会变成 '%2f52'，因为'%2f'只有特殊含义的
         * 也即是说，字符 '/'是特殊字符，encode后会转化为 '%2f'
         */
        // String METHOD = "overview/getDistrictRpt";

        // var map = new LinkedHashMap<>();
        map.put("access_token", accessToken);
        map.put("site_id", siteId);
//        map.put("start_date", START_DATE);
//        map.put("end_date", END_DATE);
//        map.put("metrics", METRICS);
//        map.put("method", METHOD);

        var stringBuilder = new StringBuilder("https://openapi.baidu.com/rest/2.0/tongji/report/getData?");
        var i = 0;
        for (Map.Entry<Object, Object> entry:map.entrySet()) {
            if(i > 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
            ++i;
        }

        var httpClient = HttpClient.newBuilder().build();

        // var string = "https://openapi.baidu.com/rest/2.0/tongji/report/getData?access_token=121.0a03f27c11c614fc51682e263c469dcb.Y7YLmLihAUy9xJ0CL8q5OBwmsh-ApxaFaspKf5L.i9FNwA&site_id=14958271&start_date=20200501&end_date=20200503&metrics=pv_count&method=overview%2FgetDistrictRpt";

        var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(stringBuilder.toString()))
                .build();
        HttpResponse<String> response ;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        Objects.requireNonNull(response);
        logger.info(response.body());
        return response.body();
    }

    public String formatDate(LocalDate localDate){
        final var formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return formatter.format(localDate);
    }

    private boolean checkIsExisted(String fieldName){
        final var s = data.get(fieldName);
        if(Objects.isNull(s)) {
            logger.info("数据未存在，请求百度API");
            return false;
        }
        try {
            final var map = objectMapper.readValue(s, HashMap.class);
            final var result = (LinkedHashMap<String, List<String>>)map.get("result");
            final var timeSpans = result.get("timeSpan").get(0).split("-");
            logger.info("统计数据已存在");
            return formatDate(LocalDate.now().minusDays(1)).equals(timeSpans[1].trim().replaceAll("/", ""));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.info("数据未存在，数据未存在，请求百度API");
            return false;
        }
    }

    private void refreshToken() {
        if(ChronoUnit.DAYS.between(refreshTokenDate, LocalDate.now()) > 20) {
            String url = "http://openapi.baidu.com/oauth/2.0/token?grant_type=refresh_token&" +
                    "refresh_token=" + refreshToken + "&" +
                    "client_id=" +  clientId + "&" +
                    "client_secret=" + clientSecret;
            try {
                var request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
                var httpClient = HttpClient.newBuilder().build();
                var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
                var map = objectMapper.readValue(response.body().getBytes(), HashMap.class);
                map.forEach((key, value) -> {
                    System.out.println(key + " : " + value);
                });
                accessToken = (String) map.get("access_token");
                refreshToken = (String) map.get("refresh_token");
                refreshTokenDate = LocalDate.now();
            } catch (URISyntaxException | InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
