package com.timi.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.timi.common.code.RespCommonCode;
import com.timi.common.exception.BusinessExceptionBuilder;
import com.timi.modules.user.controller.param.UserPasswordParam;
import com.timi.modules.user.dao.UserMapper;
import com.timi.modules.user.entity.UserEntity;
import com.timi.modules.user.holder.UserContentHolder;
import com.timi.modules.user.security.utils.SecurityUtils;
import com.timi.modules.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author hhh
 * @date 2021/1/25
 */
@Service
@Slf4j
public class UserSerivceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public BaseMapper<UserEntity> getMapper() {
        return userMapper;
    }
    @Override
    public UserEntity getUser(){
        UserEntity user = userMapper.selectById(1042);
        return user;
    }

    @Override
    public UserEntity findByUsername(String username) throws UsernameNotFoundException{
        LambdaQueryWrapper<UserEntity> queryWrapper = Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, username);
        UserEntity userEntity = userMapper.selectOne(queryWrapper);
        if (userEntity==null){
            throw new UsernameNotFoundException(username);
        }
        return userEntity;
    }

    @Override
    public Boolean updatePassword(UserPasswordParam requestParam) {
        //参数校验
        if (!requestParam.getConfirmPassword().equals(requestParam.getPassword())) {
            //密码和确认密码不一致
            throw BusinessExceptionBuilder.build(RespCommonCode.AUTH_PASSWORD_NOTSAME);
        }

        //新密码不能与老密码一样
        if (StringUtils.equals(requestParam.getOldPassword(), requestParam.getPassword())) {
            throw BusinessExceptionBuilder.build(RespCommonCode.USER_PASSWORD_CONFIG_PASSWORD_CANOT_EQUAL);
        }
        String password = SecurityUtils.getPassword(requestParam.getPassword());
        String oldPassword = SecurityUtils.getPassword(requestParam.getOldPassword());
        String username = UserContentHolder.getContext().getUsername();

        //SELECT id,password FROM user WHERE (username = ?)
        LambdaQueryWrapper<UserEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(UserEntity::getId, UserEntity::getPassword)
                .eq(UserEntity::getUsername, username);
        UserEntity user = userMapper.selectOne(queryWrapper);


        if (SecurityUtils.matches(user.getPassword(), oldPassword)) {
            throw BusinessExceptionBuilder.build(RespCommonCode.OLD_PASSWORD_ERROR);
        }
        user.setPassword(password);
        int flag = userMapper.updateById(user);
        //返回结果
        return flag > 0;
    }
}
