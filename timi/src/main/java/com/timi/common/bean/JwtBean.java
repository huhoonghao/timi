package com.timi.common.bean;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.timi.common.config.TimiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 * @Title: JWT工具类
 * @Description:
 */
@Component
public class JwtBean {

    @Autowired
    private TimiProperties timiProperties;
    public  JwtBean() {
    }

    public JwtBean(TimiProperties timiProperties) {
        this.timiProperties = timiProperties;
    }

    public TimiProperties getTimiProperties() {
        return timiProperties;
    }
    /**
     * 获取jwt的负载
     * @param token
     * @return
     */
    public Map<String, Claim> getClaim(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(timiProperties.getSecurity().getJwtSecret())).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaims();
    }

    /**
     * 获取客户端唯一标识
     * @param token
     * @return
     */
    public String getUUID(String token) {

        Claim claim = null;
        try {
            claim = getClaim(token).get("uuid");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(claim == null) {
            return null;
        }
        return claim.asString();
    }

    /**
     * 秘钥
     * @param username 用户名
     * @return
     */
    public String createToken(String username, String uuid) {
        String token = JWT.create()
                .withClaim("username", username)
                .withClaim("uuid", uuid)
                .sign(Algorithm.HMAC256(timiProperties.getSecurity().getJwtSecret()));
        return token;
    }

    /**
     * 获取用户名
     * @param token
     * @return
     */
    public String getUsername(String token) {
        Claim claim = getClaim(token).get("username");
        if(claim == null) {
            return null;
        }
        return claim.asString();
    }

    /**
     *
     * @Title: getTimeOut
     * @Description: 获取失效时间
     * @Author hhh
     * @Date 2021/5/25 15:29
     * @Param []
     * @Return java.lang.Long
     */
   public Long getTimeOut(){
        Long jwtTokenTimeout = timiProperties.getSecurity().getJwtTokenTimeout();
        return jwtTokenTimeout;
    }


}
