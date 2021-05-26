package com.timi.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * 登录事件
 */
public class UserLoginEvent extends ApplicationEvent {

    private String username;

    public UserLoginEvent(String username) {
        super(username);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
