package com.timi.common.annotation;

import java.lang.annotation.*;

/**
 * 缓存，将对应key,value加入缓存
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HashValue {
    String key();
}
