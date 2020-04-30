package com.color.pink;

import com.color.pink.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author HarrisonLee
 * @date 2020/4/30 12:59
 */
@SpringBootTest
public class TagTest {

    @Autowired
    private TagService tagService;

    @Test
    public void test01(){
        var map = tagService.selectAllIdAndTitle();
        map.forEach((key, value) -> System.out.println(key + " " + value));
    }
}
