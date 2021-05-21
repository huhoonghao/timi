package com.timi.common.annotation;


import java.lang.annotation.*;

/**
 *
 * @Title: 
 * @Description: 
 * @Author hhh
 * @Date 2021/5/20 18:27
 * @Param 
 * @Return 
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface DtoSkip {
}