package cn.dooling.samlazure.helper;

import cn.dooling.samlazure.config.jwt.JWTProperties;
import cn.dooling.samlazure.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JWTHelper {
    @Autowired
    private JWTProperties jwtProperties;

    public String createToken(String id) {
        return JwtUtils.createToken(id, jwtProperties.getExpiration(), jwtProperties.getIss(), jwtProperties.getSecret());
    }

    public String createTokenToHeader(String id, HttpServletResponse response) {
        String token = createToken(id);
        response.addHeader(jwtProperties.getHeaderName(), jwtProperties.getSchema() + " " + token);
        return token;
    }


    public String getToken(HttpServletResponse response) {
        String token = response.getHeader(jwtProperties.getHeaderName());
        if (null != token) {
            return token.replace(jwtProperties.getSchema(), "").trim();
        }
        return "";
    }


    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(jwtProperties.getHeaderName());
        if (null != token) {
            return token.replace(jwtProperties.getSchema(), "").trim();
        }
        return null;
    }


    public String getId(String token) {
        return JwtUtils.getSubject(token, jwtProperties.getSecret());
    }
}
