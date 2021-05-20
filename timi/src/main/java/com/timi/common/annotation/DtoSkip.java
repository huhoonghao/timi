package com.timi.common.annotation;


import java.lang.annotation.*;

/**
 *
 * @author lr
 * @since 2021-01-19
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface DtoSkip {
}