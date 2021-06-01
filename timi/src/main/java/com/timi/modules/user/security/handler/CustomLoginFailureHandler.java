package com.timi.modules.user.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timi.common.bean.ResponseBean;
import com.timi.common.cache.CacheHelper;
import com.timi.common.cache.RedisKeyEnum;
import com.timi.common.code.UserResponseCode;
import com.timi.common.constant.user.Enabled;
import com.timi.common.event.EventEnum;
import com.timi.common.event.UserApplicationEvent;
import com.timi.modules.user.dao.UserMapper;
import com.timi.modules.user.entity.UserEntity;
import com.timi.modules.user.holder.UserContentHolder;
import com.timi.modules.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录失败
 */
@Slf4j
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private CacheHelper cacheHelper;
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ApplicationContext applicationContext;


    /**
     * 超时时间
     */
    private Long TIME_OUT = 2L;
    private final static String ERROR_TIMES = "times";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = UserContentHolder.getContext().getUsername();
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print( "login is fail");
        log.debug("------------------{}登录失败--------------------",username);

        String code = UserResponseCode.USER_TOKEN_EXPIRED;
        String key =null ;
        ResponseBean.Builder builder = ResponseBean.builder();

        if (exception instanceof BadCredentialsException) {

            code = UserResponseCode.USER_PASSWORD_ERROR;
            key = RedisKeyEnum.USER_PASSWORD_ERROR_NUMBER.getKey() + username;
            //增加登录错误次数
            Long increment = cacheHelper.increment(key);
            //2小时过期
            cacheHelper.expire(key, TimeUnit.HOURS, TIME_OUT);
            builder.data(increment);
            //返回错误次数
            Map<String, Long> result = new HashMap<>(2);
            result.put(ERROR_TIMES, increment);

            //错误大于5次，账户锁定,发出锁定事件
            Integer errorTimes = Integer.parseInt(cacheHelper.stringGet(key));
            log.debug("错误次数{}",errorTimes);
            builder.data(result);
        }
        if (exception instanceof LockedException) {
            code = "500-02-0009";//用户账号被锁定
        }

        //账户密码已经被锁定
        if (StringUtils.isNotEmpty(cacheHelper.stringGet(key)) && Long.parseLong(cacheHelper.stringGet(key)) >= 5L) {
            //修改状态
            applicationContext.publishEvent(new UserApplicationEvent(username, EventEnum.LOCKED));
        }
        ResponseBean responseBean = builder.code(code).build();
        responseBean.setMessage("---登录失败---------->");
        response.getWriter().print( new ObjectMapper().writeValueAsString(responseBean));

    }
}
