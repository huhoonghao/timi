package com.timi.modules.user.security.file;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.timi.common.bean.JwtBean;
import com.timi.common.cache.CacheHelper;
import com.timi.common.cache.RedisKeyEnum;
import com.timi.common.constant.TimiConstant;
import com.timi.modules.user.holder.UserContentHolder;
import com.timi.modules.user.holder.UserContext;
import com.timi.common.util.JwtUtils;
import com.timi.modules.user.service.impl.CustomUserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 分布式环境中构造本地Security Authentication
 */
@Component
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    /**
     * 缓存帮助类
     */
    @Autowired
    private CacheHelper cacheHelper;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Autowired
    private JwtBean jwtBean;

  //  @Autowired
   // private FilterExceptionHandler filterExceptionHandler;

    protected AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            //当Security中存在登录标识时，直接跳过
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }
            //获取请求头中token
            String token = request.getHeader(JwtUtils.Authorization);
            //当token为空或过期时，未登录
            if(StringUtils.isBlank(token) || cacheHelper.exist(RedisKeyEnum.TOKEN_JWT_EXPIRE.getKey() + token)) {
                filterChain.doFilter(request, response);
                System.out.println("-----------------------------------------------------------------");
                return;
            }
            //从token中解析出用户名
            String username = "";
            try {
                username = jwtBean.getUsername(token);
                String tokenKey = RedisKeyEnum.USER_LOGIN_TOKEN.getKey()+ username + TimiConstant.REDIS_SPLIT + jwtBean.getUUID(token);
                //验证token是否有效//如果前端传递的token与Redis中用户对应的token不相等
                if (!cacheHelper.exist(tokenKey) || !StringUtils.equals(token,cacheHelper.stringGet(tokenKey))) {
                    throw new TokenExpiredException(String.format("The Token has expired on %s.", new Date()));
                }

                //刷新token时间
                cacheHelper.expire(tokenKey, TimeUnit.MINUTES, jwtBean.getTimiProperties().getSecurity().getJwtTokenTimeout());

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(createSuccessfulAuthentication(request, userDetails));
                SecurityContextHolder.setContext(context);

                UserContext userContext = new UserContext();
                userContext.setUsername(username);
                userContext.setAuthorities(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
                UserContentHolder.setContext(userContext);
                filterChain.doFilter(request, response);
            } catch (TokenExpiredException tokenExpiredException) {
               // filterExceptionHandler.handler(request, response, tokenExpiredException);
                return;
            } catch (Exception e) {
               // filterExceptionHandler.handler(request, response, e);
                return;
            }


        } finally {
            UserContentHolder.clearContext();
        }

    }

    /**
     * 构建成功的AuthenticationToken
     * @param request
     * @param user
     * @return
     */
    private Authentication createSuccessfulAuthentication(HttpServletRequest request,
                                                          UserDetails user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        authenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return authenticationToken;
    }

}
