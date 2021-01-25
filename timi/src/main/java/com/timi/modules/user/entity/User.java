package com.timi.modules.user.entity;

import com.timi.common.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author hhh
 * @date 2021/1/25
 */
@Data
public class User extends BaseEntity {
    private String userName;
    private String realName;
    private String password;
    private Date loginTime;
    private String email;
    private String telephone;
    /**
     * 状态：0，禁用；1，启用 -1:已删除
     */
    private Byte status;
    private String remark;
    private String ip;
}
