package com.timi.modules.user.controller.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author hhh
 * @date 2021/5/24
 */
@Data
public class UserSignInParam implements Serializable {
    @NotBlank(message = "账号不能为空")
    @Size(min = 8,message ="username at least 8 characters" )
    private String username;
    /**
     * 新密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 8,message ="Password at least 8 characters" )
    private String password;

    /**修改密码时确认密码*/
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
    private String phone;


    //真实姓名
    @NotBlank(message = "真实名字不能为空")
    private String nickname;

    //验证码
    private String SignInCode;
}
