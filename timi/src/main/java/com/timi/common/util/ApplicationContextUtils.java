package com.timi.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.springframework.context.support.AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME;

/**
 * Spring工具类
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.applicationContext = applicationContext;

        //设置事件发布异步执行
        ApplicationEventMulticaster applicationEventMulticaster = applicationContext.getBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);
        if (applicationEventMulticaster instanceof SimpleApplicationEventMulticaster) {
            SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = (SimpleApplicationEventMulticaster) applicationEventMulticaster;

            int processors = Runtime.getRuntime().availableProcessors();
            //设置异步执行
            simpleApplicationEventMulticaster
                    .setTaskExecutor(new ThreadPoolExecutor(processors,
                            2 * processors,
                            10,
                            TimeUnit.MINUTES,
                            new ArrayBlockingQueue<>(16 * processors, true)));
        }
    }

    /**
     * 根据名称和类型获取SpringBean
     * @param name
     * @param requireType
     * @param <T>
     * @return
     */
    public static  <T> T getBean(String name, Class<T> requireType) {
        return applicationContext.getBean(name, requireType);
    }

    /**
     * 根据名称和类型获取SpringBean
     * @param requireType
     * @param <T>
     * @return
     */
    public static  <T> T getBean(Class<T> requireType) {
        return applicationContext.getBean(requireType);
    }


    /**
     * 发布事件
     * @param applicationEvent 事件
     */
    public static void publishEvent(ApplicationEvent applicationEvent) {
        applicationContext.publishEvent(applicationEvent);
    }
}
