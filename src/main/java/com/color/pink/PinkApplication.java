package com.color.pink;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.color.pink.dao")
@SpringBootApplication
public class PinkApplication {

    public static void main(String[] args) {

        SpringApplication.run(PinkApplication.class, args);
    }

}
