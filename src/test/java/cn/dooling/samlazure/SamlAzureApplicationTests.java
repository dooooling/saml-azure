package cn.dooling.samlazure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class SamlAzureApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void test() {

    }

    public static void main(String[] args) {
        JwtBuilder jwtBuilder = Jwts.builder().setId("").setSubject("小白")
                .setIssuedAt(new Date())//设置签发时间
                .signWith(SignatureAlgorithm.HS256, "xiaocai");//设置签名秘钥
        String token = jwtBuilder.compact();
        System.out.println(token);

        Claims claims = Jwts.parser().parseClaimsJws(token).getBody();
        System.out.println("id:" + claims.getId());
        System.out.println("subject:" + claims.getSubject());
        System.out.println("IssuedAt:" + claims.getIssuedAt());
    }
}
