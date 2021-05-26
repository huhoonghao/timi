package com.timi.modules.user.security.handler;


import com.alibaba.fastjson.JSONObject;
import com.timi.common.bean.ResponseBean;
import com.timi.common.code.UserResponseCode;
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
 * @Title: 认证不通过
 * @Description:
 * @Author hhh
 * @Date 2021/5/26 15:04
 * @Param
 * @Return
 */
public class TimiAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private Logger logger = LoggerFactory.getLogger(AuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        logger.debug("----------------------认证不通过--------------------------");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        //用户token过期
        String code = UserResponseCode.USER_TOKEN_EXPIRED;
        ResponseBean responseBean = ResponseBean.builder().code(code).build();
        responseBean.setMessage("用户认证不通过");
        httpServletResponse.getWriter().print(JSONObject.toJSONString(responseBean));


    }
}
