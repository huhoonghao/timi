/*
package com.timi.modules.user.security.handler;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.timi.common.bean.ResponseBean;
import com.timi.common.cache.CacheHelper;
import com.timi.common.code.ResponseCode;
import com.timi.common.constant.TimiConstant;
import com.timi.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

*/
/**
 * 过滤器异常处理
 * @author lr
 * @since 2021-01-23
 *//*

public class TimiFilterExceptionHandler {

    private GaeaMessageSourceAccessor messages = GaeaSecurityMessageSource.getAccessor();

    private Logger logger = LoggerFactory.getLogger(TimiFilterExceptionHandler.class);

    @Autowired
    private CacheHelper cacheHelper;

    */
/**
     * 异常处理
     * @param request
     * @param response
     * @param e
     * @throws IOException
     *//*

    public void handler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        response.setCharacterEncoding(TimiConstant.CHARSET_UTF8);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        ResponseBean.Builder builder = ResponseBean.builder();
        ResponseBean responseBean;
        String code = ResponseCode.FAIL_CODE;
        if(e instanceof TokenExpiredException || e instanceof SignatureGenerationException || e instanceof SignatureVerificationException) {
            code = ResponseCode.USER_TOKEN_EXPIRED;
        }else if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            code = businessException.getCode();
        } else if (e instanceof Exception) {
            //登录凭证失效
            logger.error("", e);
            code = ResponseCode.FAIL_CODE;
        }

        responseBean = builder.code(code).build();
        responseBean.setMessage(messages.getMessage(code,code));
        try {
            response.getWriter().print(JSONObject.toJSONString(responseBean));
        } catch (IOException io) {

        }
    }
}
*/
