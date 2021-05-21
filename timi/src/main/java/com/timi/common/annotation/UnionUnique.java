package com.timi.common.annotation;

import java.lang.annotation.*;

/**
 * 多字段不能重复
 * @author lr
 * @since 2021-01-12
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UnionUnique {

    /**
     * 列
     * @return
     */
    String column() default "";

    /**
     * 组,必填
     * @return
     */
    String group();
}
