package com.color.pink;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PinkApplicationTests {

    @Value("${baidu.tongji.code}")
    private String code;

    @Test
    void contextLoads() {
        System.out.println(code);
    }

}
