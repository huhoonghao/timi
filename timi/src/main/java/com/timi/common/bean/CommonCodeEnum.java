package com.timi.common.bean;

/**
 * 全局常量枚举
 * @author lirui
 * @since 2020-06-21
 */
public enum CommonCodeEnum {

    RESPONSE_SUCCESS("200", "成功"),
    RESPONSE_ERROR("500", "失败"),
    CRYPTO_ERROR("5001", "解密失败");

    private String code;

    private String message;

    CommonCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
