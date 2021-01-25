package com.timi.modules.user.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.timi.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hhh
 * @date 2021/1/19
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private ConfigurableApplicationContext applicationContext;
    @NacosValue("${email.server.address}")
    private String emailServer;
    @Autowired
    private UserService userService;
    @GetMapping("query")
    public String test(){
        String supperUserName = applicationContext.getEnvironment().getProperty("supperUserName");
        String st=supperUserName+"-------------"+emailServer;
        String user = userService.getUser();
        return user;
    }
}
