package com.color.pink;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

/**
 * @author HarrisonLee
 * @date 2020/5/6 21:50
 */
public class PropertiesTest {

    @Test
    public void test01(){
        final var properties = new Properties();
        try(final var inputStream = this.getClass().getClassLoader().getResourceAsStream("login-info.properties")) {
            properties.load(inputStream);
            properties.forEach((key, value) -> {
                System.out.println(key + " : " + value);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
