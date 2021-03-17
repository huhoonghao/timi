package com.timi.modules.user.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.timi.common.exception.BusinessExceptionBuilder;
import com.timi.common.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * JWT工具类
 */
public class JwtUtils {

    public static final String Authorization = "Authorization";

    private static String SECRET = "huHongHao";

    /**
     * 秘钥
     * @param username 用户账号
     * @return
     */
    public static String createToken(String username) {
        String token = JWT.create()
                .withExpiresAt(LocalDateUtils.toDate(LocalDateTime.now().plusHours(4)))
                .withClaim("username", username)
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }

    /**
     * 根据用户名、角色、权限、菜单等信息生成token
     * @param username 用户名
     * @param roles 角色
     * @param authorities 权限
     * @param menus 菜单
     * @return
     */
    public static String createToken(String username, List<String> roles, List<String> authorities, List<String> menus) {
        String token = JWT.create()
                .withExpiresAt(LocalDateUtils.toDate(LocalDateTime.now().plusHours(4)))
                .withClaim("username", username)
                .withClaim("role", roles)
                .withClaim("authorities", authorities)
                .withClaim("menus", menus)
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }

    /**
     * 获取jwt的负载
     * @param token
     * @returnLocalDateUtils
     */
    public static Map<String, Claim> getClaim(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaims();
    }

    /**
     * 获取用户名
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        Claim claim = getClaim(token).get("username");
        if(claim == null) {
            throw BusinessExceptionBuilder.build(ErrorCode.TOKEN_USERNAME_NOT_EXIST);
        }
        return claim.asString();
    }
}
