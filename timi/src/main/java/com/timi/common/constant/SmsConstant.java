package com.timi.common.constant;

/**
 * 短信
 * @author hhh
 * @date 2021/5/31
 */
public enum SmsConstant {
    /**
     * 短信URL
     */
    TIMI_0001("TIMI_0001", "【Timi】尊敬的%{user}用户：您的验证码是:%{code},有效时间%{expire}分钟"),
    TIMI_0002("TIMI_0002", "【Timi】尊敬的%{user}用户：您于%{time}时间登录 登录地为:%{address} IP:%{ip}"),
    TIMI_003("TIMI_0003", "【Timi】尊敬的%{user}用户：您账号已被冻结,请联系客服:%{customerService}");

    String value;
    String message;
    SmsConstant(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
