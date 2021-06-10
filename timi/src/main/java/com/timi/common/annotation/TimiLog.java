package com.timi.common.annotation;

import java.lang.annotation.*;

/**
 * @author hhh
 * @date 2021/5/27
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TimiLog {
    /**
     *页面或按钮标题
     * @return 标题
     */
    String pageTitle() default "";
    /**
     * 是否保存请求的参数和响应参数
     */
    boolean isSaveRequestData() default true;
    boolean isSaveResponseData() default false;
}
