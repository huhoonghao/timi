package com.timi.modules.user.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timi.common.bean.JwtBean;
import com.timi.common.bean.ResponseBean;
import com.timi.common.cache.CacheHelper;
import com.timi.common.cache.RedisKeyEnum;
import com.timi.common.constant.TimiConstant;
import com.timi.common.event.UserLoginEvent;
import com.timi.common.util.ApplicationContextUtils;
import com.timi.common.util.TimiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 登录成功
 */
@Slf4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private CacheHelper cacheHelper;
    @Autowired
    private JwtBean jwtBean;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        log.info("{}成功登录",username);
        ResponseBean.Builder builder = ResponseBean.builder();

        //该用户独有的私钥
        //用于标识不同的客户端，当允许多点登录时，只需要删掉Redis中uuid对应的token即可，其他点登录不影响
        String uuid = TimiUtils.UUID();
        //生成token
        String token = jwtBean.createToken(username,uuid);
        //保存登录token,并默认两个小时
        cacheHelper.stringSetExpire(RedisKeyEnum.USER_LOGIN_TOKEN.getKey()+ username + TimiConstant.REDIS_SPLIT + uuid, token,
                jwtBean.getTimeOut(), TimeUnit.MINUTES);

        //删除错误次数
        cacheHelper.delete(RedisKeyEnum.USER_PASSWORD_ERROR_NUMBER.getKey() + username);
       //TODO 权限 以后再写
        ResponseBean responseBean = builder.data(token).build();
        responseBean.setMessage("success");
        response.getWriter().print( new ObjectMapper().writeValueAsString(responseBean));
        //发布登录事件
        ApplicationContextUtils.publishEvent(new UserLoginEvent(username));
        //发邮件 短信通知


    }
}
