package com.timi.modules.user.security.configuration;

import com.timi.modules.user.security.file.JwtTokenAuthenticationFilter;
import com.timi.modules.user.security.handler.CustomAccessDeniedHandler;
import com.timi.modules.user.security.handler.CustomLoginFailureHandler;
import com.timi.modules.user.security.handler.CustomLoginSuccessHandler;
import com.timi.modules.user.security.handler.TimiAuthenticationEntryPoint;
import com.timi.modules.user.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * security 安全配置
 */
@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;
    /**
     * 自定义，从数据库获取用户信息
     *
     * @return
     */
    @Bean
    public UserDetailsService apUserDetailsService() {
        return new CustomUserDetailsService();
    }

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 自定义userDetailService
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     /*   //未加密解密
        auth.userDetailsService(apUserDetailsService())
                .passwordEncoder(NoOpPasswordEncoder.getInstance());*/
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 未登录
     *
     * @return
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }


    /**
     * 登录成功处理
     *
     * @return
     */
    @Bean
    public CustomLoginSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }

    /**
     * 登录失败处理
     * @return
     */
    @Bean
    public CustomLoginFailureHandler customLoginFailureHandler() {
        return new CustomLoginFailureHandler();
    }

    /**
     * 权限校验未通过处理
     *
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new TimiAuthenticationEntryPoint();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling()
                //未登录
                .accessDeniedHandler(accessDeniedHandler())
                //权限校验未通过处理
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                //为了防止跨站提交攻击，通常会配置csrf。 如果不采用csrf，可禁用security的csrf csrf().disable()
                .csrf().disable()
                //不会保存session状态
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login","/logout", "/health").permitAll()
                .anyRequest().authenticated()
                .and()
                //登录成功
                .formLogin().successHandler(customLoginSuccessHandler())
                //登录失败
                .failureHandler(customLoginFailureHandler());
                //登录逻辑处理
                 http.addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }


}
