package com.timi.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * timi配置信息
 * hhh
 */
@Component
@ConfigurationProperties(prefix = "spring.tm")
@RefreshScope
public class TimiProperties {

    /**
     * 加密私钥，用于加密信息
     */
    private String secret = "huHongHao-timi";

    /**
     * 权限相关
     */
    private Security security = new Security();

    /**
     * 订阅了哪些组件
     */
    private String subscribes;

    /**
     * 用于获取请求信息的服务applicationName值
     */
    private List<String> requestInfoServiceIds;


    public class Security {

        /**
         * jwt秘钥
         */
        private String jwtSecret = "huHongHao";

        /**
         * jwt的token超时时间，单位分钟
         */
        private Long jwtTokenTimeout = 120L;

        /**
         * 扫描权限时，是否只扫描带注解的，默认扫描所有
         */
        private boolean scanAnnotation = false;
        /**
         * 白名单，跳过
         */
        private List<String> whileList;

        /**
         * 当false时，当请求路径未配置时，不放过，true时当未配置时，放过
         */
        private boolean permitAll = false;



        public boolean isPermitAll() {
            return permitAll;
        }

        public boolean isScanAnnotation() {
            return scanAnnotation;
        }

        public void setScanAnnotation(boolean scanAnnotation) {
            this.scanAnnotation = scanAnnotation;
        }

        public void setPermitAll(boolean permitAll) {
            this.permitAll = permitAll;
        }

        public List<String> getWhileList() {
            return whileList;
        }

        public void setWhileList(List<String> whileList) {
            this.whileList = whileList;
        }

        public String getJwtSecret() {
            return jwtSecret;
        }

        public void setJwtSecret(String jwtSecret) {
            this.jwtSecret = jwtSecret;
        }

        public Long getJwtTokenTimeout() {
            return jwtTokenTimeout;
        }

        public void setJwtTokenTimeout(Long jwtTokenTimeout) {
            this.jwtTokenTimeout = jwtTokenTimeout;
        }
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSubscribes() {
        return subscribes;
    }

    public void setSubscribes(String subscribes) {
        this.subscribes = subscribes;
    }

    public List<String> getRequestInfoServiceIds() {
        return requestInfoServiceIds;
    }

    public void setRequestInfoServiceIds(List<String> requestInfoServiceIds) {
        this.requestInfoServiceIds = requestInfoServiceIds;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }
}
