package com.timi.modules.user.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.timi.common.base.BaseController;
import com.timi.common.base.BaseService;
import com.timi.common.bean.ResponseBean;
import com.timi.common.cache.CacheHelper;
import com.timi.common.cache.RedisKeyEnum;
import com.timi.modules.resource.service.ResourceService;
import com.timi.modules.role.service.RoleService;
import com.timi.modules.user.controller.dto.UserDTO;
import com.timi.modules.user.controller.param.UserParam;
import com.timi.modules.user.controller.param.UserPasswordParam;
import com.timi.modules.user.entity.UserEntity;
import com.timi.modules.user.holder.UserContentHolder;
import com.timi.modules.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.codec.ServerSentEvent.builder;

/**
 * @author hhh
 * @date 2021/1/19
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController <UserParam, UserEntity, UserDTO>{
    @Autowired
    private ConfigurableApplicationContext applicationContext;
    @NacosValue("${email.server.address}")
    private String emailServer;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private CacheHelper cacheHelper;

    @Override
    public BaseService<UserParam, UserEntity> getService() {
        return userService;
    }
    @Override
    public UserDTO getDTO() {
        return new UserDTO();
    }
    @Override
    public UserEntity getEntity() {
        return new UserEntity();
    }


    @GetMapping("/query")
    public UserEntity test(){
        String supperUserName = applicationContext.getEnvironment().getProperty("supperUserName");
        String st=supperUserName+"-------------"+emailServer;
        System.out.println("---------------------------------------------------"+st);
        UserEntity user = userService.getUser();
        return user;
    }
    /**
     * 获取用户信息包括权限和表单
     * @param token
     * @return
     */
    @RequestMapping("/userInfo")
    public ResponseBean userAuth(@RequestHeader("Authorization") String token) {
        String username = UserContentHolder.getContext().getUsername();

        //获取当前用户所拥有的角色
        List<String> userRoles = roleService.getUserRoles(username);
        //获取当前用户的权限
        Set<String> userAuthorities = resourceService.getUserAuthorities(userRoles);

        //获取当前用户所拥有的菜单
       // List<ResourceEntity> userMenus = resourceService.getUserMenus(userRoles);

        UserEntity userEntity = userService.findByUsername(username);
        Map<String,Object> userInfo = new HashMap<>(8);
      //  userInfo.put("allMenuUrls", resourceService.getAllMenuUrls(userRoles));
     //   userInfo.put("menus",userMenus);
        userInfo.put("authorities",userAuthorities);
        userInfo.put("roles",userRoles);
        userInfo.put("username",username);
        userInfo.put("nickname",userEntity.getNickname());
        String applyNo = cacheHelper.hashGetString(RedisKeyEnum.USER_APPLY.getKey(), username);

        if (StringUtils.isNotBlank(applyNo)) {
            userInfo.put("applyNo", applyNo);
        }
        ResponseBean listResponseBean = ResponseBean.builder().data(userInfo).build();
        return listResponseBean;
    }
    /**
     * 用户修改密码
     * @param reqParam
     * @return
     */
    @PostMapping("/updatePassword")
    public ResponseBean updatePassword(@Validated @RequestBody UserPasswordParam reqParam){
        return responseSuccessData(userService.updatePassword(reqParam));
    }





}
