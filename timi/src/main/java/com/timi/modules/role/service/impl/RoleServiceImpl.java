package com.timi.modules.role.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.timi.modules.role.dao.UserRoleMapper;
import com.timi.modules.role.dao.entity.UserRoleEntity;
import com.timi.modules.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 角色相关service的实现类
 * @author lirui
 * @since 2020-06-13
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<String> getUserRoles(String username) {
        LambdaQueryWrapper<UserRoleEntity> queryWrapper = Wrappers.<UserRoleEntity>lambdaQuery().eq(UserRoleEntity::getUsername, username);
        return userRoleMapper.selectList(queryWrapper)
                .stream()
                .map(userRoleEntity -> userRoleEntity.getRoleCode())
                .collect(Collectors.toList());
    }
}
