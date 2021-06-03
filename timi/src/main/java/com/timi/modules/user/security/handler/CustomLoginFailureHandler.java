package com.timi.modules.user.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timi.common.bean.ResponseBean;
import com.timi.common.cache.CacheHelper;
import com.timi.common.cache.RedisKeyEnum;
import com.timi.common.code.RespCommonCode;
import com.timi.common.event.EventEnum;
import com.timi.common.event.UserApplicationEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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
    private ApplicationContext applicationContext;
    @Autowired
    private MessageSource messageSource;


    /**
     * 超时时间
     */
    private Long TIME_OUT = 2L;
    private final static String ERROR_TIMES = "times";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print( "login is fail");
        log.debug("------------------{}登录失败--------------------",username);
        String code = RespCommonCode.LOGIN_FAILED;
        String key =null ;
        ResponseBean.Builder builder = ResponseBean.builder();

        if (exception instanceof BadCredentialsException) {
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
        //初始化message
        String message=messageSource.getMessage(RespCommonCode.LOGIN_FAILED, null, LocaleContextHolder.getLocale());
        if (StringUtils.isNotEmpty(cacheHelper.stringGet(key)) && Long.parseLong(cacheHelper.stringGet(key)) >= 5L) {
            //修改状态
            applicationContext.publishEvent(new UserApplicationEvent(username, EventEnum.LOCKED));
        }
        //用户账号被冻结
        if (exception instanceof LockedException) {
            code = RespCommonCode.ACCOUNT_FREEZE;
            message=messageSource.getMessage(RespCommonCode.ACCOUNT_FREEZE, null, LocaleContextHolder.getLocale());
        }
        //用户不可用，注销
        if (exception instanceof DisabledException) {
            code = RespCommonCode.USER_DISABLED_ERROR;
            message=messageSource.getMessage(RespCommonCode.USER_DISABLED_ERROR, null, LocaleContextHolder.getLocale());
        }
        ResponseBean responseBean = builder.code(code).build();
        responseBean.setMessage(message);
        response.getWriter().print( new ObjectMapper().writeValueAsString(responseBean));

    }

}
