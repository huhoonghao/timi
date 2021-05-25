package com.timi.common.util;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.timi.common.annotation.DtoSkip;
import com.timi.common.annotation.FormatterType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author hhh
 * @date 2021/5/20
 */
public abstract class TimiUtils {

    private static final Logger logger = LoggerFactory.getLogger(TimiUtils.class);
    /**
     * 驼峰转下划线
     * @param source
     * @return
     */
    public static String camelToUnderline(String source) {
        return StringUtils.camelToUnderline(source);
    }
    /**
     * 将对象转换为Json字符串
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        return JSONObject.toJSONString(object);

    }

    /**
     * 复制集合
     * @param sourceList
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(List<? extends Object> sourceList, Class<T> targetClass) {
        return sourceList.stream().map(source -> {
            try {
                T target = targetClass.newInstance();
                copyAndFormatter(source, target);
                return target;
            } catch (Exception e) {
                logger.error("集合翻译失败,目标类：" + targetClass.getName(), e);
            }
            return null;
        }).collect(Collectors.toList());
    }



    /**
     * 字段类型转换
     *
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T copyAndFormatter(Object source, T target) {
        //获取目标类并翻译
        Field[] declaredFields = target.getClass().getDeclaredFields();
        List<Field> fields = new ArrayList<>(declaredFields.length);

        //跳过翻译的字段
        List<String> skipFields = new ArrayList<>();

        //非普通类型的翻译即对象，需要翻译其对象
        List<Field> formatterTypeFields = new ArrayList<>();

        //过滤掉DtoSkip注解的字段
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(DtoSkip.class)) {
                skipFields.add(field.getName());
                continue;
            }

            if (field.isAnnotationPresent(FormatterType.class)) {
                formatterTypeFields.add(field);
                continue;
            }

            fields.add(field);
        }
        //都要放过
        skipFields.addAll(formatterTypeFields.stream().map(Field::getName).collect(Collectors.toList()));

        //entity翻译成DTO,跳过忽略DtoSkip的字段和类型翻译FormatterType的字段
        BeanUtils.copyProperties(source, target, skipFields.toArray(new String[0]));

        Field[] superDeclaredFields = target.getClass().getSuperclass().getDeclaredFields();
        fields.addAll(Arrays.asList(superDeclaredFields));

        //遍历字段，找出 Formatter注解注释的字段,并翻译
      //  formatterHandler(source,target, fields);
        //翻译非基础类型即：对象或者集合

        for (Field field : formatterTypeFields) {
            try {
                PropertyDescriptor sourcePropertyDescriptor = new PropertyDescriptor(field.getName(), source.getClass());
                PropertyDescriptor targetPropertyDescriptor = new PropertyDescriptor(field.getName(), target.getClass());
                Object fieldSource = sourcePropertyDescriptor.getReadMethod().invoke(source);
                if (fieldSource == null) {
                    continue;
                }
                Method writeMethod = targetPropertyDescriptor.getWriteMethod();
                switch (field.getAnnotation(FormatterType.class).type()) {
                    case OBJECT:
                        Object fieldTarget = field.getType().newInstance();
                        copyAndFormatter(fieldSource, fieldTarget);
                        writeMethod.invoke(target, fieldTarget);
                        break;
                    case LIST:
                        Type genericType = field.getGenericType();
                        ParameterizedType parameterizedType = (ParameterizedType) genericType;
                        Class fieldTargetClass = (Class) parameterizedType.getActualTypeArguments()[0];
                        List fieldTargetList = copyList((List) fieldSource, fieldTargetClass);
                        writeMethod.invoke(target, fieldTargetList);
                        break;
                    default:
                }

            } catch (Exception e) {
                logger.error("FormatterType处理异常", e);
                continue;
            }
        }

        return target;
    }

    /**
     * 匹配关键字，替换成真正文本
     */
    public static Pattern pattern = Pattern.compile("\\$\\{(\\w+)}");
    /**
     * 格式化替换，如${username} is ${age} 替换成 admin is 24
     * @param source
     * @param map  替换参数
     * @return
     */
    public static String replaceFormatString(String source, Map<String,Object> map) {

        Matcher matcher = pattern.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String variable = matcher.group(1);
            Object value = map.get(variable);
            if (value != null) {
                matcher.appendReplacement(sb, String.valueOf(value));
            }

        }
        return sb.toString();
    }

    /**
     * 获取UUID去掉横杠
     * @return
     */
    public static String UUID() {
        return UUID.randomUUID().toString().replaceAll("\\-","");
    }

}
