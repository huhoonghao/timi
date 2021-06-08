package com.timi.modules.log.dao;

import com.timi.common.base.TimiBaseMapper;
import com.timi.modules.log.entity.TimiLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author hhh
 * @date 2021/6/8
 */
@Mapper
public interface TimiLogMapper  extends TimiBaseMapper<TimiLogEntity> {
}
