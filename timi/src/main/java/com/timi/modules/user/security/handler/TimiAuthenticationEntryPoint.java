package com.timi.modules.user.security.handler;


import com.alibaba.fastjson.JSONObject;
import com.timi.common.bean.ResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 认证不通过
 * @author lr
 * @since 2021-01-26
 */
public class TimiAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private Logger logger = LoggerFactory.getLogger(AuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        //用户token过期
        String code = "User.token.expired";
        ResponseBean responseBean = ResponseBean.builder().code(code).build();
        responseBean.setMessage("用户认证不通过");
        httpServletResponse.getWriter().print( JSONObject.toJSONString(responseBean));


    }
}
