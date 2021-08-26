package com.timi.modules.user.security.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author hhh
 * @date 2021/5/24
 */
public class SecurityUtils {


    /**
     * 保存登录信息
     */
    public static ThreadLocal<JSONObject> loginInfoThreadLocal = new ThreadLocal<>();

    /**
     * 获取密码
     * @param password
     * @return
     */
    public static String getPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }


    /**
     * 密码匹配
     * @param str
     * @param oldStr
     * @return
     */
    public static boolean matches(String str,String oldStr){
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(str,oldStr);
    }

}
