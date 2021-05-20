package com.timi.common.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.timi.common.base.BaseEntity;

/**
 * @author hhh
 * @date 2021/1/25
 */
public interface TimiBaseMapper<T extends BaseEntity> extends BaseMapper<T> {
}
