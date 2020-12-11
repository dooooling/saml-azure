package cn.dooling.samlazure.controller;

import cn.dooling.samlazure.config.saml.SamlProperties;
import cn.dooling.samlazure.domain.dto.LoginFormDTO;
import cn.dooling.samlazure.domain.dto.ResponseDTO;
import cn.dooling.samlazure.domain.dto.RegisterFormDTO;
import cn.dooling.samlazure.helper.JWTHelper;
import cn.dooling.samlazure.service.RedisService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
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


    @PostMapping("/register")
    public ResponseDTO<Object> registerUser(@RequestBody RegisterFormDTO dto) {
        return new ResponseDTO<>(dto);
    }

    @PostMapping("/saml/login")
    public ResponseDTO<Object> samlLogin(HttpServletResponse response) {
        String loginId = UUID.randomUUID().toString();
        boolean result = redisService.set(SamlProperties.SAML_LOGIN_ID_KEY_PREFIX + loginId, "", 300);
        Cookie cookie = new Cookie(SamlProperties.SAML_LOGIN_STATE_NAME, loginId);
        cookie.setMaxAge(5 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new ResponseDTO<>(result, loginId);
    }

    @RequestMapping("/saml/result/{loginId}")
    public ResponseDTO<Object> samlLoginResult(@PathVariable String loginId) {
        String key = SamlProperties.SAML_LOGIN_ID_KEY_PREFIX + loginId;
        boolean hasKey = redisService.hasKey(key);
        if (hasKey) {
            String s = redisService.get(key);
            if (StrUtil.isNotBlank(s)) {
                redisService.del(key);
                LoginFormDTO loginFormDTO = JSONUtil.toBean(s, LoginFormDTO.class);
                return new ResponseDTO<>(true, jwtHelper.createToken(loginFormDTO.getUsername(), loginFormDTO.getPassword()));
            }
            return new ResponseDTO<>(310, false);
        }
        return new ResponseDTO<>(311, false);
    }

}
