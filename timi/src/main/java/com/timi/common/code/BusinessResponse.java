package com.timi.common.code;

/**
 * @author hhh
 * @date 2021/5/27
 */
public interface BusinessResponse {

    //手机号码为空
    String PHONE_IS_NULL="user.phone.is.null";
    //两次密码不一样
    String PASSWORDS_ARE_DIFFERENT="user.passwords.are.different";
    //已注册
    String REGISTERED="user.registered";
    //手机验证码错误
    String SIGN_IN_CODE="user.sign.code.error";
    //新老密码不能一样
    String USER_PASSWORD_CONFIG_PASSWORD_CANT_EQUAL="user.password.is.cannot";
    //密码错误
    String OLD_PASSWORD_ERROR="user.password.error";


}
