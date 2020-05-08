package com.color.pink;

import com.color.pink.service.AboutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author HarrisonLee
 * @date 2020/5/8 10:39
 */
@SpringBootTest
public class AboutTest {
    @Autowired
    private AboutService aboutService;

    @Test
    public void testSave() {
        String markdown = "#123";
        String html = "<h1>123</h1>";
        final var b = aboutService.saveContent(markdown, html);
        System.out.println(b);
    }

    @Test
    public void testGet() {
        final var markdown = aboutService.getMarkdown();
        final var html = aboutService.getHtml();
        System.out.println(markdown);
        System.out.println(html);
    }
}
