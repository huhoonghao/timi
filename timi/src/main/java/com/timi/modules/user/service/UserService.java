package com.timi.modules.user.service;

import com.timi.common.base.BaseService;
import com.timi.modules.user.controller.param.UserParam;
import com.timi.modules.user.controller.param.UserPasswordParam;
import com.timi.modules.user.controller.param.UserSignInParam;
import com.timi.modules.user.entity.UserEntity;

/**
 * @author hhh
 * @date 2021/1/25
 */
public interface UserService  extends BaseService<UserParam,UserEntity> {
    UserEntity getUser();

    UserEntity findByUsername(String username);
    
    /**
     *
     * @Title: updatePassword
     * @Description: 修改密码
     * @Author hhh
     * @Date 2021/5/24 14:05
     * @Param [requestParam]
     * @Return java.lang.Boolean
     */
    Boolean updatePassword(UserPasswordParam requestParam);

    UserEntity signIn(UserSignInParam reqParam);

    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    UserEntity getUserByUsername(String username);

    Boolean signInSendCode(String phone);
    
}
