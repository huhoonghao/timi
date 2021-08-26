package com.timi.common.code;
public class ResponseCode {

    /**
     * 成功响应码
     */
    public static String SUCCESS_CODE = "200";

    /**
     * 失败响应码
     */

    public static String FAIL_CODE = "500";
    /**
     * 未查询到对象
     */
    public static String RECORD_NO_EXIST = "500-0001";


    /**
     * 插入失败
     */
    public static String INSERT_FAILURE = "Insert.failure";

    /**
     * 更新失败
     */
    public static String UPDATE_FAILURE = "Update.failure";

    /**
     * 删除失败
     */
    public static String DELETE_FAILURE = "Delete.failure";

    /**
     * 违反唯一索引约束
     */
    public static String UNIQUE_KEY = "500-0005";

    /**
     * 存在多条记录
     */
    public static String MULTI_RECORDS = "500-0006";


}
