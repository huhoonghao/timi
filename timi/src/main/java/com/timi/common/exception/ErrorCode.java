package com.timi.common.exception;

/**
 * 异常码
 * 标准：状态(错误：500,成功：200)-模块代码(2位)-具体异常码（4位）
 * 通用：模块代码 00
 * business：模块代码01
 * user: 模块代码02
 *
 * 比如：
 * 通用模块：500-00-00001
 * business模块: 500-01-0001
 * user模块： 500-02-0001
 * @since 2020-07-02
 */
public interface ErrorCode {
    /**
     * jwt解析token中用户名不存在
     */
    String TOKEN_USERNAME_NOT_EXIST = "500-00-0002";
}
