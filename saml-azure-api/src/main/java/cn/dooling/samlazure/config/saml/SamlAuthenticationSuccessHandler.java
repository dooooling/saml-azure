package cn.dooling.samlazure.config.saml;

import cn.dooling.samlazure.controller.AuthController;
import cn.dooling.samlazure.domain.dto.LoginFormDTO;
import cn.dooling.samlazure.domain.dto.ResponseDTO;
import cn.dooling.samlazure.service.RedisService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * saml2 登陆成功处理
 */
@Component
public class SamlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private RedisService redisService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        String samlLoginId = Arrays.stream(cookies).filter(cookie -> SamlProperties.SAML_LOGIN_STATE_NAME.equals(cookie.getName())).findFirst().get().getValue();
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        if (StrUtil.isNotBlank(samlLoginId)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
            redisService.set(SamlProperties.SAML_LOGIN_ID_KEY_PREFIX + samlLoginId, JSONUtil.toJsonStr(new LoginFormDTO(userDetails.getUsername(), userDetails.getPassword())), 300);
            Cookie cookie = new Cookie(SamlProperties.SAML_LOGIN_STATE_NAME, "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
            responseDTO.setMsg("saml login success.");
            responseDTO.setSuccess(true);
        } else {
            responseDTO.setSuccess(false);
            responseDTO.setMsg("saml login failed.");
        }
        ServletUtil.write(response, JSONUtil.toJsonStr(responseDTO), ContentType.JSON.toString());
    }
}
