package com.timi.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * 用户锁定事件
 */
public class UserApplicationEvent extends ApplicationEvent {

    /**
     * 用户名
     */
    private String username;

    /**
     * 事件枚举
     */
    private EventEnum eventEnum;

    public UserApplicationEvent(String username, EventEnum eventEnum) {
        super(username);
        this.username = username;
        this.eventEnum = eventEnum;
    }

    public String getUsername() {
        return username;
    }


    public EventEnum getEventEnum() {
        return eventEnum;
    }

}
