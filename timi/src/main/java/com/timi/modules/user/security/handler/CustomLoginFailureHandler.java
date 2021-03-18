package com.timi.modules.user.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timi.common.bean.ResponseBean;
import com.timi.common.cache.CacheHelper;
import com.timi.common.cache.RedisKeyEnum;
import com.timi.modules.user.constant.Enabled;
import com.timi.modules.user.dao.UserMapper;
import com.timi.modules.user.entity.UserEntity;
import com.timi.modules.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 登录成功
 */
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private CacheHelper cacheHelper;
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        System.out.println("登录失败了---------------->");
        response.getWriter().print( "login is fail");

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        //获取请求中的用户名
        String username = request.getParameter("username");
  /*      try {
            username = CryptoUtils.desEncrypt(username).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        String code = "500-02-0004";//用户token 过期
        String key =null ;
        ResponseBean.Builder builder = ResponseBean.builder();
        if (exception instanceof BadCredentialsException) {
            code = "500-02-0005";//用户密码错误
            key = RedisKeyEnum.USER_PASSWORD_ERROR_NUMBER.getKey() + username;
            //增加登录错误次数
            Long increment = cacheHelper.increment(key);
            cacheHelper.expire(key, TimeUnit.HOURS, 2L);
            builder.content(increment);
        }
        if (exception instanceof LockedException) {
            code = "500-02-0009";//用户账号被锁定
        }
        //账户密码已经被锁定
        if (StringUtils.isNotEmpty(cacheHelper.stringGet(key)) && Long.parseLong(cacheHelper.stringGet(key)) >= 5L) {
            //修改状态
            UserEntity byUsername = userService.findByUsername(username);
            UserEntity userEntity =new UserEntity();
            userEntity.setId(byUsername.getId());
            userEntity.setAccountLocked(Enabled.YES.getValue());
            userMapper.updateById(userEntity);
        }
        ResponseBean responseBean = builder.code(code).build();
        responseBean.setMessage("登录失败");
       // responseBean.setMessage(messageSourceHolder.getMessage(code));
        response.getWriter().print( new ObjectMapper().writeValueAsString(responseBean));

    }
}
