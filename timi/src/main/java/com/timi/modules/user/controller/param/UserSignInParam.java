package com.timi.modules.user.controller.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author hhh
 * @date 2021/5/24
 */
@Data
public class UserSignInParam implements Serializable {
    private String username;
    /**
     * 新密码
     */
    @NotBlank(message = "密码不能为空")
   // @Size(min = 8,message ="Password at least 8 characters" )
    private String password;

    /**修改密码时确认密码*/
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    private String phone;

    private String nickname;
}
