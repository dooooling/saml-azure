package cn.dooling.samlazure.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {


    /**
     * 创建toke
     *
     * @param username
     * @param expiration
     * @param iss
     * @param secret
     * @return
     */
    public static String createToken(String username, long expiration, String iss, String secret) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secret)
                .setIssuer(iss)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    /**
     * 从token中获取用户名
     *
     * @param token
     * @param secret
     * @return
     */

    /**
     * 是否已过期
     */
    public static boolean isExpiration(String token, String secret) {
        return getTokenBody(token, secret).getExpiration().before(new Date());
    }

    private static Claims getTokenBody(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getSubject(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}
