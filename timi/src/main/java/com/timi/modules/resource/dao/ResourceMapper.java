package com.timi.modules.resource.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.timi.modules.resource.dao.entity.ResourceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资源数据库操作
 */
@Mapper
public interface ResourceMapper extends BaseMapper<ResourceEntity> {

}
