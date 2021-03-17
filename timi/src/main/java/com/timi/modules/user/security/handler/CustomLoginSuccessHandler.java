package com.timi.modules.user.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timi.common.cache.CacheHelper;
import com.timi.common.bean.ResponseBean;
import com.timi.common.cache.RedisKeyEnum;
import com.timi.modules.user.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * 登录成功
 */
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private CacheHelper cacheHelper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("登录成功了-------------------------->");
        ResponseBean.Builder builder = ResponseBean.builder();
        String username = authentication.getName();
        //该用户独有的私钥
        String token = JwtUtils.createToken(username);

        //保存登录token
        cacheHelper.hashSet(RedisKeyEnum.TOKEN_JWT_USER.getKey(), username, token);

        //保存权限并清空旧的权限
        String[] authorities =
                authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
                        .toArray(new String[]{});
        cacheHelper.setAdd(RedisKeyEnum.USER_AUTHORITIES.getKey() + username, authorities, true);

        ResponseBean responseBean = builder.content(token).build();
        responseBean.setMessage("success");
        response.getWriter().print( new ObjectMapper().writeValueAsString(responseBean));
    }
}
