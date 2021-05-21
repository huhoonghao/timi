package com.timi.common.annotation;

import java.lang.annotation.*;

/**
 * 字段
 * @since 2021-01-12
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormatterType {

    FormatterEnum type() default FormatterEnum.OBJECT;
}