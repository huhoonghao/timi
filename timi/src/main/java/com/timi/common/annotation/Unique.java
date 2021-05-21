package com.timi.common.annotation;

import java.lang.annotation.*;

/**
 * 单一字段唯一索引校验
 *
 * @since 2021-01-12
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
