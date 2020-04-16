package com.color.pink;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

/**
 * @author HarrisonLee
 * @date 2020/4/16 22:28
 */
public class SetTest {

    @Test
    void test01(){
        var set1 = new HashSet<String>();
        var set2 = new HashSet<String>();

        set1.add("Ubuntu");
        set1.add("CentOS7");
        set1.add("LeetCode");


        set2.add("Ubuntu");
        set2.add("CentOS8");
        set2.add("PAT");

//        System.out.println("并集");
//        set1.addAll(set2);
//        set1.forEach(System.out::println);

//        System.out.println("交集");
//        set1.retainAll(set2);
//        set1.forEach(System.out::println);

//        System.out.println("在set1中，不在set2中的元素");
//        set1.removeAll(set2);
//        set1.forEach(System.out::println);

//        System.out.println("在set2中，不在set1中的元素");
//        set2.removeAll(set1);
//        set2.forEach(System.out::println);

        // 差集
        var result = new HashSet<String>();
        result.addAll(set1);
        result.addAll(set2);
        set1.retainAll(set2);
        result.removeAll(set1);
        result.forEach(System.out::println);
    }

    @Test
    void test2() {
        var set1 = new HashSet<String>();
        set1.add("java");
        var set2 = new HashSet<String>();
        set2.add("java");
        set2.add("spring");
        set2.add("mybatis");
        set2.retainAll(set1);
        set2.forEach(System.out::println);
    }
}
