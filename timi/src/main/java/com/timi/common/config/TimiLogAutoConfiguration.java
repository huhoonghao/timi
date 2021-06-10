package com.timi.common.config;

import com.timi.common.annotation.TimiImportantLogAspects;
import org.springframework.context.annotation.Bean;

/**
 * 日志组件
 *
 * @author lr
 * @since 2021-01-15
 */
public class TimiLogAutoConfiguration {
    @Bean
    public TimiImportantLogAspects timiLogAspects() {
        return new TimiImportantLogAspects();
    }
}
