package cn.dooling.samlazure.module;

import cn.dooling.samlazure.config.filter.JWTConfig;
import cn.dooling.samlazure.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JWTHelper {
    @Autowired
    private JWTConfig jwtConfig;

    public String createToken(String id) {
        return JwtUtils.createToken(id, jwtConfig.getExpiration(), jwtConfig.getIss(), jwtConfig.getSecret());
    }

    public String createTokenToHeader(String id, HttpServletResponse response) {
        String token = createToken(id);
        response.addHeader(jwtConfig.getHeaderName(), jwtConfig.getSchema() + " " + token);
        return token;
    }


    public String getToken(HttpServletResponse response) {
        String token = response.getHeader(jwtConfig.getHeaderName());
        if (null != token) {
            return token.replace(jwtConfig.getSchema(), "").trim();
        }
        return "";
    }


    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(jwtConfig.getHeaderName());
        if (null != token) {
            return token.replace(jwtConfig.getSchema(), "").trim();
        }
        return null;
    }


    public String getId(String token) {
        return JwtUtils.getSubject(token, jwtConfig.getSecret());
    }
}
