package com.color.pink.util;

public class CamelUtils {

    public static String convert(String str){
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }
}
