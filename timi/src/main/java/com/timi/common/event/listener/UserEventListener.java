package com.timi.common.event.listener;

import com.timi.common.constant.user.Enabled;
import com.timi.common.event.EventEnum;
import com.timi.common.event.UserApplicationEvent;
import com.timi.modules.user.entity.UserEntity;
import com.timi.modules.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

/**
 * 监听用户事件
 */
@Slf4j
public class UserEventListener implements ApplicationListener<UserApplicationEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(UserApplicationEvent event) {
        log.info("监听到用户锁定事件--->");
        UserEntity user = userService.getUserByUsername(event.getUsername());
        EventEnum eventEnum = event.getEventEnum();
        switch (eventEnum) {
            //锁定
            case LOCKED:
                log.info("监听到用户锁定事件-------->正在处理");
                user.setAccountLocked(Enabled.YES.getValue());
                break;
            //密码过期
            case CERDENTIALS_EXPIRE:
                user.setCredentialsNonExpired(Enabled.NO.getValue());
                break;
            default:
        }
        //更新用户状态
        userService.update(user);
    }
}
