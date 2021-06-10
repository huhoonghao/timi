package com.timi.common.config;

import com.timi.common.annotation.TimiImportantLogAspects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

/**
 * 日志组件
 */
@Configuration
public class TimiLogAutoConfiguration {
    @Bean
    public TimiImportantLogAspects timiLogAspects() {
        return new TimiImportantLogAspects();
    }
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
