package com.timi.modules.log.controller;

import com.timi.common.base.BaseController;
import com.timi.common.base.BaseService;
import com.timi.modules.log.controller.dto.TimiLogDTO;
import com.timi.modules.log.controller.param.TimiLogParam;
import com.timi.modules.log.entity.TimiLogEntity;
import com.timi.modules.log.service.TimiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hhh
 * @date 2021/6/8
 */
@RestController
@RequestMapping("/log")
public class TimiLogController extends  BaseController <TimiLogParam, TimiLogEntity, TimiLogDTO> {
    @Autowired
    private TimiLogService timiLogService;

    @Override
    public BaseService<TimiLogParam, TimiLogEntity> getService() {
        return timiLogService;
    }

    @Override
    public TimiLogDTO getDTO() {
        return new TimiLogDTO();
    }

    @Override
    public TimiLogEntity getEntity() {
        return new TimiLogEntity();
    }
}
