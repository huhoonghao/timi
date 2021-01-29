package com.timi.modules.role.service;


import java.util.List;

/**
 * 角色相关service
 * @author lirui
 * @since 2020-06-13
 */
public interface RoleService {

    /**
     * 获取指定用户的角色
     * @param username
     * @return
     */
    List<String> getUserRoles(String username);

}
