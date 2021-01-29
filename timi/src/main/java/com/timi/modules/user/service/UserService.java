package com.timi.modules.user.service;

import com.timi.modules.user.entity.UserEntity;

/**
 * @author hhh
 * @date 2021/1/25
 */
public interface UserService  {
    UserEntity getUser();

    UserEntity findByUsername(String username);
}
