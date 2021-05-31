package com.timi.common.annotation;

import java.lang.annotation.*;

/**
 * @author hhh
 * @date 2021/5/27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Phone {
    String message() default "";
}
