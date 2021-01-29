package com.timi.modules.user.dao;

import com.timi.common.mybatisPlus.TimiBaseMapper;
import com.timi.modules.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author hhh
 * @date 2021/1/25
 */
@Mapper
public interface UserMapper extends TimiBaseMapper<UserEntity> {
}
