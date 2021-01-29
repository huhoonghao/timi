package com.timi.modules.resource.dao.entity;

import com.timi.common.mybatisPlus.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色与资源关联表
 * @author lirui
 * @since 2020-06-13
 */
@Data
public class RoleResourceEntity extends BaseEntity implements Serializable {

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 资源编码
     */
    private String resCode;

    /**
     * 0:权限，1：菜单
     */
    private Integer isMenu;
}
