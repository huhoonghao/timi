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
public class UserPasswordParam  implements Serializable {
    private String username;
    /**
     * 新密码
     */
    @NotBlank(message = "password not empty")
   // @Size(min = 8,message ="Password at least 8 characters" )
    private String password;
    /**修改密码时旧密码*/
    @NotBlank(message = "oldPassword not empty")
    private String oldPassword;

    /**修改密码时确认密码*/
    @NotBlank(message = "confirmPassword not empty")
    private String confirmPassword;
}
