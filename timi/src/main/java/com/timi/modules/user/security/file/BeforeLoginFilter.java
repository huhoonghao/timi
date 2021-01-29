package com.timi.modules.user.security.file;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author hhh
 * @date 2021/1/29
 */
@Configuration
public class BeforeLoginFilter  extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("执行了BeforeLoginFilter");
        // 继续调用 Filter 链
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
