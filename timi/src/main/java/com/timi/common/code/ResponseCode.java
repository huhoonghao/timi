package com.timi.common.code;
public final class ResponseCode {

    /**
     * 成功响应码
     */
    public static final String SUCCESS_CODE = "200";

    /**
     * 失败响应码
     */

    public static final String FAIL_CODE = "500";
    /**
     * 未查询到对象
     */
    public static final String RECORD_NO_EXIST = "500-0001";


    /**
     * 插入失败
     */
    public static final String INSERT_FAILURE = "Insert.failure";

    /**
     * 更新失败
     */
    public static final String UPDATE_FAILURE = "Update.failure";

    /**
     * 删除失败
     */
    public static final String DELETE_FAILURE = "Delete.failure";

    /**
     * 违反唯一索引约束
     */
    public static final String UNIQUE_KEY = "500-0005";

    /**
     * 存在多条记录
     */
    public static final String MULTI_RECORDS = "500-0006";


    /**
     * 用户token过期
     */
    public static final String USER_TOKEN_EXPIRED = "User.token.expired";

}
