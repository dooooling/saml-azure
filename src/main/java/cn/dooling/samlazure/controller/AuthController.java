package cn.dooling.samlazure.controller;

import cn.dooling.samlazure.module.JWTHelper;
import cn.dooling.samlazure.module.common.domain.ResponseDTO;
import cn.dooling.samlazure.module.common.service.RedisService;
import cn.dooling.samlazure.module.user.domain.dto.RegisterFormDTO;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private JWTHelper jwtHelper;

    public static final String SAML_LOGIN_ID_KEY_PREFIX = "saml:login:id:";

    @PostMapping("/register")
    public ResponseDTO<Object> registerUser(@RequestBody RegisterFormDTO dto) {
        return new ResponseDTO<>(dto);
    }

    @PostMapping("/saml/login")
    public ResponseDTO<Object> samlLogin(HttpServletResponse response) {
        String loginId = UUID.randomUUID().toString();
        boolean result = redisService.set(SAML_LOGIN_ID_KEY_PREFIX + loginId, "", 300);
//        response.addHeader();
        Cookie cookie = new Cookie("samlLoginId", loginId);
        cookie.setMaxAge(5 * 60);
        cookie.setPath("/");
        cookie.setDomain("saml-azure.com");
        response.addCookie(cookie);
        return new ResponseDTO<>(result, loginId);
    }

    @RequestMapping("/saml/result/{loginId}")
    public ResponseDTO<Object> samlLoginResult(@PathVariable String loginId) {
        String key = SAML_LOGIN_ID_KEY_PREFIX + loginId;
        boolean hasKey = redisService.hasKey(key);
        if (hasKey) {
            String s = redisService.get(key);
            if (StrUtil.isNotBlank(s)) {
                redisService.del(key);
                return new ResponseDTO<>(true, jwtHelper.createToken(s));
            }
            return new ResponseDTO<>(310, false);
        }
        return new ResponseDTO<>(311, false);
    }

}
