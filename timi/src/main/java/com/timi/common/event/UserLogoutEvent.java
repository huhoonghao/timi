package com.timi.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * 登出事件
 */
public class UserLogoutEvent extends ApplicationEvent {


    private String username;

    public UserLogoutEvent(String username) {
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
