package com.color.pink;

import com.color.pink.service.ConfigService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author HarrisonLee
 * @date 2020/5/8 23:04
 */
@SpringBootTest
public class ConfigServiceTest {

    @Autowired
    private ConfigService configService;

    @Test
    public void test01() {
        final var backendConfig = configService.getBackendConfig();
        backendConfig.forEach((key, value) -> {
            System.out.println(key + " : " +value);
        });
        backendConfig.put("name", "后台设置测试");
        configService.saveBackendConfig(backendConfig);
    }

    @Test
    public void test02() {
        final var frontendConfig = configService.getFrontendConfig();
        frontendConfig.forEach((key, value) -> {
            System.out.println(key + " : " +value);
        });
    }
}
