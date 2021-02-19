package com.timi.modules.role.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.timi.modules.role.dao.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色数据库操作
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleEntity> {


}
