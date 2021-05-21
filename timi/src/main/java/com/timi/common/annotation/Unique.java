package com.timi.common.annotation;

import java.lang.annotation.*;

/**
 *
 * @Description: 单一字段唯一索引校验
 * @Author hhh
 * @Date 2021/5/21 10:36
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Unique {
    /**
     * 数据库对应列，不指定时，将对应字段的驼峰转换为下划线（对应数据库列）
     *
     * @return
     */
    String column() default "";

    /**
     * 当违反唯一索引时，返回的错误码
     *
     * @return
     */
    String code() default "";
}
