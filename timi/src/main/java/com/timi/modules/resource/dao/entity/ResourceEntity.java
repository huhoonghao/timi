package com.timi.modules.resource.dao.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.timi.common.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单
 *
 **/
@Data
@TableName("resource")
public class ResourceEntity extends BaseEntity implements Serializable {

    private String resName;
    private String resCode;
    private String resUrl;
    private Integer isMenu;
    private Integer sortNo;
    private String icon;
    private Integer enabled;
    private String parentCode;
    private Integer parentFlag;
    @TableField(exist = false)
    private List<ResourceEntity> children;
    @TableField(exist = false)
    private List<String> subResCodes;
}
