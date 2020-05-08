package com.color.pink.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * @author HarrisonLee
 * @date 2020/5/8 10:30
 */
@Service
public class AboutService {

    private final String MF = "properties/about.md";
    private final String HF = "properties/about.html";

    public String getMarkdown(){
        try (final var m = this.getClass().getClassLoader().getResourceAsStream(MF)){
            assert m != null;
            return new String(m.readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getHtml(){
        try (final var h = this.getClass().getClassLoader().getResourceAsStream(HF)){
            assert h != null;
            return new String(h.readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveContent(String markdown, String html) {
        final File M;
        final File H;
        FileWriter MFO = null;
        FileWriter HFO = null;
        try {
            M = new ClassPathResource(MF).getFile();
            H = new ClassPathResource(HF).getFile();
            MFO = new FileWriter(M);
            MFO.write(markdown);
            HFO = new FileWriter(H);
            HFO.write(html);
            MFO.flush();
            HFO.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if(Objects.nonNull(MFO)) {
                    MFO.close();
                }
                if(Objects.nonNull(HFO)) {
                    HFO.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
