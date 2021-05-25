package com.timi.modules.user.security.handler;

import com.timi.modules.user.holder.UserContentHolder;
import com.timi.modules.user.security.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author hhh
 * @date 2021/5/24
 *  重写UsernamePasswordAuthenticationFilter，增加验证码并对用户名密码进行解密
 */
public class AuthEnableUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private Logger logger = LoggerFactory.getLogger(UsernamePasswordAuthenticationFilter.class);

    public AuthEnableUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                    AuthenticationSuccessHandler successHandler,
                                                    AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationManager(authenticationManager);

        super.setAuthenticationSuccessHandler(successHandler);

        super.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
        //清空
        UserContentHolder.clearContext();
        //清空登录信息
        SecurityUtils.loginInfoThreadLocal.remove();
    }






}
