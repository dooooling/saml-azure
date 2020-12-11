package cn.dooling.samlazure.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

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
    public static String createToken(String username, long expiration, String iss, String secret, Map<String, Object> claims) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setIssuer(iss)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000));
        Optional.ofNullable(claims).ifPresent(map -> map.forEach(jwtBuilder::claim));
        return jwtBuilder.compact();
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

    public static Claims getTokenBody(String token, String secret) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getSubject(String token, String secret) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

}
