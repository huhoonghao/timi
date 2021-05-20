package com.timi.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.timi.modules.user.dao.UserMapper;
import com.timi.modules.user.entity.UserEntity;
import com.timi.modules.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserEntity findByUsername(String username) {
        LambdaQueryWrapper<UserEntity> queryWrapper = Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, username);
        UserEntity userEntity = userMapper.selectOne(queryWrapper);
        //当用户不存在时，抛出异常
        if (userEntity == null) {
            throw new RuntimeException();
        }

        return userEntity;
    }
}
