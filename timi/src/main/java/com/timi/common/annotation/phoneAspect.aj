package com.timi.common.annotation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hhh
 * @date 2021/5/27
 */
@Component
@Slf4j
@Aspect
public class phoneAspect {
    @Pointcut("@annotation(com.timi.common.annotation.Phone)")
    public void logPointCut() {
    }

    @Before(value = "logPointCut()")
    public Object phoneCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("check---------------------------------");
        log.info("进入到环绕通知中");
        Object[] args = joinPoint.getArgs();//2.传参

        Field[] declaredFields = args.getClass().getDeclaredFields();
        List<Field> fields = new ArrayList<>(declaredFields.length);
        //过滤掉DtoSkip注解的字段
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Phone.class)) {
                log.info("找到");
            }
        }

        return null;
    }
}