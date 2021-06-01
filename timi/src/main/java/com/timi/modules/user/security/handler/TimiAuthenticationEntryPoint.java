package com.timi.modules.user.security.handler;


import com.alibaba.fastjson.JSONObject;
import com.timi.common.bean.ResponseBean;
import com.timi.common.code.UserResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

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
@Slf4j
public class TimiAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        log.debug("----------------------认证不通过--------------------------");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        //用户token过期
        String code = UserResponseCode.USER_TOKEN_EXPIRED;
        ResponseBean responseBean = ResponseBean.builder().code(code).build();
        responseBean.setMessage("用户认证不通过------------------------------");
        httpServletResponse.getWriter().print(JSONObject.toJSONString(responseBean));


    }
}
