package com.common.core.utils;

import com.app.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import java.util.Date;

public class JwtUtils {
    // 48小时后失效
    private static final long expire = 48;
    // 设置秘钥
    private static final String secret = "spring";

    /** 生成token
     * @param user 用户信息
     * @return 生成的token
     */
    public static String createToken(User user) {
        Date now = new Date();
//        Date expiration = new Date(now.getTime() + 3600*1000*expire);
        Date expiration = new Date(now.getTime() + 10*1000);// 测试用，10s后过期
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
//                .setSubject(user.getAccount())
//                .claim("role", user.getRole())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /** 解析token
     * @param token 要解析的token
     * @return 解析结果
     */
    public static Claims parseToken(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /** 判断token是否过期
     * @param token 要判断的token
     * @return 是否过期
     */
    public static boolean isExpired(String token) {
        try {
            parseToken(token).getSubject();
            return false;
        }
        catch(ExpiredJwtException e) {
            return true;
        }
    }
}
