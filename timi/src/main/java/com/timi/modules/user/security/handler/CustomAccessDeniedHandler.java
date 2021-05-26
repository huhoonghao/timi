package com.timi.modules.user.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timi.common.bean.ResponseBean;
import com.timi.common.code.UserResponseCode;
import com.timi.common.constant.TimiConstant;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 没登录处理
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println("未登录----------------->");
        response.setCharacterEncoding(TimiConstant.CHARSET_UTF8);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String code = UserResponseCode.USER_NO_AUTH;
        ResponseBean responseBean = ResponseBean.builder().code(code).build();
        responseBean.setMessage("未登录----------------->"+responseBean);
        response.getWriter().print( new ObjectMapper().writeValueAsString(responseBean));

    }
}
