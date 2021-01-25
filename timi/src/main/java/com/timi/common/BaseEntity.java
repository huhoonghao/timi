package com.timi.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 基础实体类
 * @author lr
 * @since 2020-03-23
 */
@Data
public class BaseEntity {

    /**
     * 记录ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 创建用户
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新用户
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 版本号
     */
    @TableField(fill = FieldFill.INSERT, update="%s+1")
    private Long version;
}
