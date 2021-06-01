package com.timi.common.test;


import com.timi.common.base.BaseEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hhh
 * @date 2021/5/12
 */

public class StreamUtil {

    /*
    * ListString遍历属性去空格
    * */


    static void ListString(){
        List<String> strings = Arrays.asList("abc", "abc", "", "bc", "efg", "abcd","", "jkl","   ");
        //去空格
        List<String> stringListTrim =strings.stream().filter(s -> !s.trim().isEmpty()).collect(Collectors.toList());
        System.out.println(stringListTrim);
        //删除list中的Abc
        List<String> delAbc = strings.stream().filter(s -> s.equals("abc")).collect(Collectors.toList());
        System.out.println(delAbc);
        //获取字符串最长的那个
        strings.stream().max(Comparator.comparing(String::length));
        int sum = strings.stream().mapToInt(x->x.length()).sum();
        int max = strings.stream().mapToInt(x->x.length()).max().getAsInt();
        int min = strings.stream().mapToInt(x->x.length()).min().getAsInt();
        double avg = strings.stream().mapToInt(x->x.length()).average().getAsDouble();
        System.out.println("最大值："+max+"\n最小值："+min+"\n总和："+sum+"\n平均值："+avg);


    }



    static void ListProperty(){
        List<BaseEntity> baseEntityList=new ArrayList<>();
        for (int i=0;i<5;i++){
            BaseEntity baseEntity=new BaseEntity();
            baseEntity.setId(1L+i);
            baseEntity.setCreateBy("hhh");
            baseEntity.setUpdateBy("");
            baseEntity.setVersion(100+i);
            baseEntityList.add(baseEntity);
        }

        //查找ID大于2的人
        List<BaseEntity> ids = baseEntityList.stream().filter(x -> x.getId() > 2).collect(Collectors.toList());
        List<String> collect = baseEntityList.stream().filter(x -> x.getId() > 2).map(baseEntity -> baseEntity.getCreateBy()).collect(Collectors.toList());

        //查找ID最大的人
        Optional<BaseEntity> max = baseEntityList.stream().max(Comparator.comparing(BaseEntity::getId));
        System.out.println(max.get());
        System.out.println(max.get().getCreateBy());
        // 求总数
        Long count = baseEntityList.stream().collect(Collectors.counting());
        //求最大
        baseEntityList.stream().map(BaseEntity::getVersion).collect(Collectors.maxBy(Integer::compare));
        //求平均
        baseEntityList.stream().collect(Collectors.averagingLong(BaseEntity::getVersion));
        //求和
        baseEntityList.stream().collect(Collectors.summarizingLong(BaseEntity::getVersion));

        //版本号是否大于2的分组
        Map<Boolean, List<BaseEntity>> map = baseEntityList.stream().collect(Collectors.partitioningBy(x -> x.getVersion() > 2));
        System.out.println("版本号是否大于2的分组"+map);
        Map<String, Map<Integer, List<BaseEntity>>> collect1 = baseEntityList.stream().collect(Collectors.groupingBy(x -> x.getCreateBy(), Collectors.groupingBy(y -> y.getVersion())));
        System.out.println("将员工先按性别分组，再按地区分组"+collect1);
        //排序升序
        List<BaseEntity> collect2 = baseEntityList.stream().sorted(Comparator.comparing(BaseEntity::getVersion)).collect(Collectors.toList());
        System.out.println("升序排序"+collect2);
        //升序排序 显示名字
        List<String> collect3 = baseEntityList.stream().sorted(Comparator.comparing(BaseEntity::getVersion)).map(BaseEntity::getCreateBy).collect(Collectors.toList());
        System.out.println("升序排序显示名字"+collect3);
        //降序排序
        List<String> collect4 = baseEntityList.stream().sorted(Comparator.comparing(BaseEntity::getVersion).reversed()).map(b -> b.getCreateBy()).collect(Collectors.toList());
        System.out.println(collect4);

        //先按version 排序 再按照ID 升序

    }
}
