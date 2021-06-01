package com.timi.common.event.listener;

import com.timi.common.constant.user.Enabled;
import com.timi.common.event.EventEnum;
import com.timi.common.event.UserApplicationEvent;
import com.timi.modules.user.entity.UserEntity;
import com.timi.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

/**
 * 监听用户事件
 */
public class UserEventListener implements ApplicationListener<UserApplicationEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(UserApplicationEvent event) {

        UserEntity user = userService.getUserByUsername(event.getUsername());
        EventEnum eventEnum = event.getEventEnum();
        switch (eventEnum) {
            //锁定
            case LOCKED:
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
