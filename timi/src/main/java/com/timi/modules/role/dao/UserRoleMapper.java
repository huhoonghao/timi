package com.timi.modules.role.dao;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.timi.modules.role.dao.entity.UserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色数据库操作

 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleEntity> {

}
