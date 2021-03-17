package com.timi.modules.user.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.timi.modules.user.entity.UserEntity;
import com.timi.modules.user.service.SpringContextUtil;
import com.timi.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author hhh
 * @date 2021/1/19
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ConfigurableApplicationContext applicationContext;
    @NacosValue("${email.server.address}")
    private String emailServer;
    @Autowired
    private UserService userService;
    @GetMapping("/query")
    public UserEntity test(){
        String supperUserName = applicationContext.getEnvironment().getProperty("supperUserName");
        String st=supperUserName+"-------------"+emailServer;
        System.out.println("---------------------------------------------------"+st);
        UserEntity user = userService.getUser();
        return user;
    }

    @GetMapping("testAppliContext")
    public Object test2() throws Exception {
        Object bean = SpringContextUtil.getBean("userSerivceImpl");
        Method methods = bean.getClass().getMethod("getUser");
        Object invoke = methods.invoke(bean);
        System.out.println("---------------------------------------------------"+invoke);
   /*     if(checkObjFieldIsNull(invoke)){
            System.out.println(".............");
        }*/
        return bean;
    }


    public  boolean checkObjFieldIsNull(Object obj) throws IllegalAccessException {
        boolean flag = false;
        for(Field f : obj.getClass().getDeclaredFields()){
            f.setAccessible(true);
            if(f.getName().equals("username")){
                Object o = f.get(obj);
                if(o!=null){
                    return flag;
                }
            }

        }
        return true;
    }

    public static void main(String[] args) {
        String processNode="air";
       String a= processNode.substring(0, 1).toUpperCase() + processNode.substring(1);
        System.out.println(a);
    }
}
