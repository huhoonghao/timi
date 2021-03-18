package com.timi.modules.user.security.file;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.timi.common.cache.CacheHelper;
import com.timi.common.cache.RedisKeyEnum;
import com.timi.modules.user.holder.UserContentHolder;
import com.timi.modules.user.holder.UserContext;
import com.timi.modules.user.security.utils.JwtUtils;
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
import java.util.stream.Collectors;

/**
 * 分布式环境中构造本地Security Authentication
 *
 * @since 2020-06-20
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
                return;
            }
            //从token中解析出用户名
            String username = "";
            try {

                username = JwtUtils.getUsername(token);
                String tokenKey = RedisKeyEnum.TOKEN_JWT_USER.getKey();
                String redisToken = cacheHelper.hashGetString(tokenKey, username);

                //如果前端传递的token与Redis中用户对应的token不相等
                if (!StringUtils.equals(token, redisToken)) {
                    filterChain.doFilter(request, response);
                    return;
                }
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
