package com.timi.modules.user;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("query")
    public String test(){
        String supperUserName = applicationContext.getEnvironment().getProperty("supperUserName");
        String st=supperUserName+"-------------"+emailServer;
        return st;
    }
}
