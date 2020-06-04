package com.color.pink;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.color.pink.dao")
@SpringBootApplication
@EnableScheduling
@EnableEncryptableProperties
public class PinkApplication extends SpringBootServletInitializer {
// public class PinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(PinkApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PinkApplication.class);
    }

}
