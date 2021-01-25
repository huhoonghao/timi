package com.timi.modules.user.service.impl;

import com.timi.modules.user.dao.UserMapper;
import com.timi.modules.user.entity.User;
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
    public String getUser(){
        User user = userMapper.selectById(1);
        return user.getEmail();
    }

}
