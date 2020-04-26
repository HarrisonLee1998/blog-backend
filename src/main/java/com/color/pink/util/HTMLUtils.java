package com.color.pink.util;

import org.springframework.stereotype.Component;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.*;

@Component
public class HTMLUtils extends HTMLEditorKit.ParserCallback {

    private StringBuffer s;

    public void parse(String str) throws IOException {
        InputStream iin = new ByteArrayInputStream(str.getBytes());
        Reader in = new InputStreamReader(iin);
        s = new StringBuffer();
        ParserDelegator delegator = new ParserDelegator();
        // the third parameter is TRUE to ignore charset directive
        delegator.parse(in, this, Boolean.TRUE);
        iin.close();
        in.close();
    }
    public void handleText(char[] text, int pos) {
        s.append(text);
    }

    public String getText() {
        return s.toString();
    }

    public static  String handleParse(String html){
        HTMLUtils htmlUtils=new HTMLUtils();
        try {
            htmlUtils.parse(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlUtils.getText().toString();
    }
}