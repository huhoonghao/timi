package com.timi.modules.role.dao.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.timi.common.mybatisPlus.BaseEntity;
import lombok.Data;

@Data
@TableName("user_role")
public class UserRoleEntity extends BaseEntity {

    /**
     * 用户名
     */
    private String username;

    /**
     * 角色编码
     */
    private String roleCode;
}
