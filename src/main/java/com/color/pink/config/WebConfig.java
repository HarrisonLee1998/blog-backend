package com.color.pink.config;

import com.color.pink.interceptor.LoginControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author HarrisonLee
 * @date 2020/5/3 4:52
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginControl loginControl;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginControl);
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowCredentials(true)
//                .allowedOrigins("http://localhost:3001")
//                .allowedHeaders("*")
//                .allowedMethods("*");
//    }
}
