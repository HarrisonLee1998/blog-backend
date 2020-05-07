package com.color.pink;

import com.color.pink.service.StatisticsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author HarrisonLee
 * @date 2020/5/5 9:32
 */
@SpringBootTest
public class StatisticsTest {

    @Autowired
    private StatisticsService service;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void test01(){
        System.out.println(service.getProvincePVCount());
    }

    @Test
    public void test02(){
        System.out.println(service.getWorldPVCount());
    }

    @Test
    public void test03() throws JsonProcessingException {
        String s = "{\"result\":{\"timeSpan\":[\"2020\\/04\\/28 - 2020\\/05\\/04\"],\"fields\":[\"simple_date_title\",\"visitor_count\"],\"items\":[[[\"2020\\/04\\/28\"],[\"2020\\/04\\/29\"],[\"2020\\/04\\/30\"],[\"2020\\/05\\/01\"],[\"2020\\/05\\/02\"],[\"2020\\/05\\/03\"],[\"2020\\/05\\/04\"]],[[\"--\"],[\"--\"],[\"--\"],[\"--\"],[\"--\"],[2],[3]],[],[]]}}";

        // final var uvTrend = service.getUVTrend(7);
        // System.out.println(uvTrend);
        final var map = objectMapper.readValue(s, HashMap.class);
        final var result = (LinkedHashMap<String, List<String>>)map.get("result");
        final var timeSpans = result.get("timeSpan").get(0).split("-");
        Arrays.stream(timeSpans).forEach(e -> {
            e = e.trim();
            final var s1 = e.replaceAll("/", "");
            System.out.println(s1);
        });
    }
}
