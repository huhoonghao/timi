package com.timi.modules.log.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.timi.modules.log.dao.TimiLogMapper;
import com.timi.modules.log.entity.TimiLogEntity;
import com.timi.modules.log.service.TimiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hhh
 * @date 2021/6/8
 */
@Service
public class TimiLogServiceImpl implements TimiLogService {
    @Autowired
    private TimiLogMapper timiLogMapper;
    @Override
    public BaseMapper<TimiLogEntity> getMapper() {
        return timiLogMapper;
    }

}
