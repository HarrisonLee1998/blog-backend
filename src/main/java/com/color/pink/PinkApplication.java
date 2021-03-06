package com.color.pink;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.color.pink.dao")
@SpringBootApplication
@EnableScheduling
//public class PinkApplication extends SpringBootServletInitializer {
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//       return application.sources(PinkApplication.class);
//    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(PinkApplication.class, args);
//    }

public class PinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(PinkApplication.class, args);
    }
}
