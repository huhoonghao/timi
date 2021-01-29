package com.timi.modules.resource.service.impl;


import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.timi.modules.resource.dao.RoleResourceMapper;
import com.timi.modules.resource.dao.entity.RoleResourceEntity;
import com.timi.modules.resource.service.ResourceService;
import com.timi.modules.user.constant.Enabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * 菜单管理管理Service
 */
@Service
public class ResourceServiceImpl implements ResourceService {


    @Autowired
    private RoleResourceMapper roleResourceMapper;
    /**
     * 获取角色对应的权限
     *
     * @param roles
     * @return
     */
    @Override
    public Set<String> getUserAuthorities(List<String> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return new HashSet<>();
        }
        LambdaQueryWrapper<RoleResourceEntity> queryWrapper = Wrappers.<RoleResourceEntity>lambdaQuery()
                .eq(RoleResourceEntity::getIsMenu, Enabled.NO.getValue())
                .in(RoleResourceEntity::getRoleCode, roles);

        return roleResourceMapper.selectList(queryWrapper)
                .stream()
                .map(RoleResourceEntity::getResCode)
                .collect(Collectors.toSet());
    }
}
