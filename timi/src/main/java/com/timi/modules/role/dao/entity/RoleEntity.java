package com.timi.modules.role.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.timi.common.mybatisPlus.BaseEntity;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("role")
public class RoleEntity extends BaseEntity implements Serializable {
    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 是否有效
     */
    private Integer enabled;

    /**
     * 备注
     */
    private String remark;
}


