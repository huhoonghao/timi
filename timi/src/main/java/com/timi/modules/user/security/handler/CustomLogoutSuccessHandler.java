package com.timi.modules.user.security.handler;


import com.alibaba.fastjson.JSONObject;
import com.timi.common.bean.JwtBean;
import com.timi.common.bean.ResponseBean;
import com.timi.common.cache.CacheHelper;
import com.timi.common.cache.RedisKeyEnum;
import com.timi.common.code.UserResponseCode;
import com.timi.common.constant.TimiConstant;
import com.timi.common.event.UserLogoutEvent;
import com.timi.common.util.ApplicationContextUtils;
import com.timi.modules.user.holder.UserContentHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出成功
 */
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private CacheHelper cacheHelper;
    @Autowired
    private JwtBean jwtBean;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print( "login is out");
        //用户名
        String username = UserContentHolder.getContext().getUsername();
        String token = request.getHeader(TimiConstant.Authorization);
        String uuid = jwtBean.getUUID(token);
        if(StringUtils.isNotBlank(token)) {
            //删除指定客户端的缓存token
            cacheHelper.delete(RedisKeyEnum.USER_LOGIN_TOKEN.getKey() + username + TimiConstant.REDIS_SPLIT + uuid);
        }

        String code = UserResponseCode.USER_LOGOUT_SUCCESS;
        ResponseBean responseBean = ResponseBean.builder().build();
        responseBean.setMessage("login is out");
        response.getWriter().print(JSONObject.toJSONString(responseBean));

        //发布登录事件
        ApplicationContextUtils.publishEvent(new UserLogoutEvent(username));
    }
}
