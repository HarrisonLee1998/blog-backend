package com.color.pink;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author HarrisonLee
 * @date 2020/4/26 0:44
 */
@SpringBootTest
public class RegexTest {
    @Test
    public void test01(){
        String r = "^-?[0-9]+$";
        System.out.println("-1".matches(r));
        System.out.println("1".matches(r));
        System.out.println(String.valueOf(Integer.MAX_VALUE).matches(r));
    }

    @Test
    public void test02(){
        String r = "^[1-9]\\d*$";
        System.out.println("0".matches(r));
        System.out.println("2".matches(r));
        System.out.println("20".matches(r));
        System.out.println("32".matches(r));
    }
}
