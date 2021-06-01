package com.timi.common.cache;

/**
 * redis中的key
 *
 */
public enum RedisKeyEnum {

    /**
     * 作用：JWT本身的token失效（私钥共享）有缺陷，登出后token仍然有效
     * 故设置每个用户独享一个加密私钥，当用户登出后，改变私钥
     */
    TOKEN_JWT_EXPIRE("timi:token:EXPIRE:", "用户存储失效的token"),



    /**
     * 作用：JWT本身的token失效（私钥共享）有缺陷，登出后token仍然有效
     * 故设置每个用户独享一个加密私钥，当用户登出后，改变私钥
     */
    //TOKEN_JWT_USER("timi:user:token", "存放用户名及对应的token"),

    /**
     * 密码错误次数
     */
    USER_PASSWORD_ERROR_NUMBER("timi:user:password:errorNumber:", "密码错误次数"),


    /**
     * 保存用户对应的权限
     */
    USER_AUTHORITIES("timi:user:authorities:", "保存用户对应的权限"),

    /**
     * 用户与供方对应关系
     */
    USER_APPLY("timi:base:user:apply", "用户与供方对应关系"),

    /**
     * 系统登录token
     */
    USER_LOGIN_TOKEN ("timi:login:token:", "登录token"),


    /**
     * 注册验证码
     */
    USER_SIG_NIN_CODE ("timi:sms:code:", "注册验证码");

    public String key;
    public String message;

    RedisKeyEnum(String key, String message) {
        this.key = key;
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }
}
