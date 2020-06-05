package com.color.pink;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JasyptTest {

    @Value("${jasypt.encryptor.password}")
    private String password;
    
    @Value("${jasypt.encryptor.algorithm}")
    private String algorithm;

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void test01() {
    }
}
