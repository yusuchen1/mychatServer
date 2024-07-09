package com.wanglongxiang.mychat.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    public static String createJwt(String secretKey, long ttl, Map<String,Object> claims){
//        指定签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//        设置过期时间
        long expTime = System.currentTimeMillis() + ttl;
        Date exp = new Date(expTime);

        JwtBuilder jwtBuilder = Jwts.builder()
                .addClaims(claims)
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                .setExpiration(exp);
        return jwtBuilder.compact();
    }

    public static Claims parseJWT(String secretKey,String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token).getBody();
        return claims;
    }
}
