package com.timi.modules.user.service;

import com.timi.common.base.BaseService;
import com.timi.modules.user.controller.param.UserParam;
import com.timi.modules.user.entity.UserEntity;

/**
 * @author hhh
 * @date 2021/1/25
 */
public interface UserService  extends BaseService<UserParam,UserEntity> {
    UserEntity getUser();

    UserEntity findByUsername(String username);
}
