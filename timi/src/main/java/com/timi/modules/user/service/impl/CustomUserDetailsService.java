package com.timi.modules.user.service.impl;


import com.timi.modules.resource.service.ResourceService;
import com.timi.modules.role.service.RoleService;
import com.timi.modules.user.constant.Enabled;
import com.timi.modules.user.entity.UserEntity;
import com.timi.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义security的UserDetailsService,从数据库获取用户信息
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ResourceService resourceService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户
        UserEntity userEntity = userService.findByUsername(username);
        //获取当前用户所拥有的角色
        List<String> userRoles = roleService.getUserRoles(username);
        //获取当前用户的权限
        List<GrantedAuthority> authorities = resourceService.getUserAuthorities(userRoles).stream()
                .map(roleCode -> {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roleCode);
                    return grantedAuthority;
                }).collect(Collectors.toList());

        //构建security的用户对象
        //账号是否可用
        boolean enabled = Enabled.YES.getValue().equals(userEntity.getEnabled());
       /* //账号是否未锁定
        boolean accountNonLocked = Enabled.NO.getValue().equals(userEntity.getAccountLocked());
        //账号是否过期
        boolean accountNonExpired = Enabled.YES.getValue().equals(userEntity.getAccountNonExpired());
        //密码是否过期
        boolean credentialsNonExpired = Enabled.YES.getValue().equals(userEntity.getCredentialsNonExpired());*/

        return new User(userEntity.getUsername(), userEntity.getPassword(),authorities);
    }
}
