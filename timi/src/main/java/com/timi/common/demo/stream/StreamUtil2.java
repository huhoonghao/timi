package com.timi.common.demo.stream;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hhh
 * @date 2021/5/12
 */
public class StreamUtil2 {
    static  void StringS(){
        List<String> list = Arrays.asList("adnm", "admmt", "pot", "xbangd", "weoujgsd");
        //Optional[weoujgsd]
        Optional<String> max = list.stream().max(Comparator.comparing(String::length));
        System.out.println(max);
        //OptionalInt[8]
        OptionalInt max1 = list.stream().mapToInt(x -> x.length()).max();
        System.out.println(max1);
        //英文字符串数组的元素全部改为大写
        String[] strArr = { "abcd", "bcdd", "defde", "fTr" };
        List<String> strList = Arrays.stream(strArr).map(String::toUpperCase).collect(Collectors.toList());
        List<String> collect = list.stream().map(String::toUpperCase).collect(Collectors.toList());
    }
}
