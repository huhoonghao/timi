package com.timi.modules.log.controller.dto;

import com.timi.common.base.BaseDTO;

import java.util.Date;

/**
 * @author hhh
 * @date 2021/6/8
 */
public class TimiLogDTO extends BaseDTO {
    private String userName;

    /**
     * 请求路径
     */
    private String requestUrl;

    /**
     * 页面或按钮标题
     */
    private String pageTitle;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 响应参数
     */
    private String responseParam;

    /**
     * 来源IP
     */
    private String sourceIp;

    /**
     * 请求方式get/post/put/delete
     */
    private String requestMethod;

    /**
     * 访问时间
     */
    private Date requestTime;

}
