package com.timi.modules.user.security.handler;

import com.timi.modules.user.holder.UserContentHolder;
import com.timi.modules.user.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hhh
 * @date 2021/6/3
 */
@Slf4j
public class TimiUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //AuthenticationManager该方法接收一个认证令牌对象，也就是认证请求作为参数，如果其中的信息匹配到目标账号，则该方法返回同一个认证令牌对象，不过其中被认证过程填充了更多的账号信息，比如授权和用户详情等。
    public TimiUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
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

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);

        return super.attemptAuthentication(request, response);
    }
}
