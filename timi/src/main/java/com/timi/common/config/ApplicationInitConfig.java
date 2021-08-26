package com.timi.common.config;

import com.timi.common.init.ApplicationInitRunner;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 程序确定初始化配置
 * @author hhh
 * @date 2021/6/18
 */
@Configuration
public class ApplicationInitConfig {
    /**
     * 应用启动后执行，用于初始化请求信息到权限表中
     * @return
     */
    @Bean
    public ApplicationInitRunner applicationInitRunner() {
        return new ApplicationInitRunner();
    }
}
