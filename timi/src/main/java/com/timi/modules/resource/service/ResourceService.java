package com.timi.modules.resource.service;


import java.util.List;
import java.util.Set;

/**
 * 菜单管理管理Service
 */
public interface ResourceService{

    Set<String> getUserAuthorities(List<String> roles);

}
