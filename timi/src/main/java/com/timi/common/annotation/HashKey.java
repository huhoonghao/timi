package com.timi.common.annotation;

import java.lang.annotation.*;

/**
 * 缓存，将对应key,value加入缓存
 * @author hhh
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HashKey {

    /**
     * 缓存对应的key
     */
    String key();

    /**
     * 当key中存在${}占位符时，需要此注解
     * @return
     */
    String[] replace() default {};
}
