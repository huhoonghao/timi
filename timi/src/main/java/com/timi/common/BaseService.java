package com.timi.common;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 基础Service,添加一些公共方法
 */
public interface BaseService<P extends PageParam,T extends BaseEntity> extends IService<T> {

    /**
     * 获取直接操作数据库接口
     * @return
     */
    TimiBaseMapper<T> getMapper();

    /**
     * 根据id查询记录
     * @param id
     * @return
     */
    default T selectOneById(Long id) {
        return getMapper().selectById(id);
    }
}
