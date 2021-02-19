package com.timi.common.mybatisPlus;

import lombok.Data;

/**
 * 分页参数
 */
@Data
public class PageParam {

    /**
     * 页数
     */
    private Integer pageNumber = 1;

    /**
     * 每页的记录数
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String order;

    /**
     * 升序还是降序
     */
    private String sort;
    
    public int getOffset() {
        return (pageNumber - 1) * pageSize;
    }
}
