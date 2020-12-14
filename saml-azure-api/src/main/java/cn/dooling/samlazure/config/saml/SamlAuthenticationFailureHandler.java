package cn.dooling.samlazure.config.saml;

import cn.dooling.samlazure.domain.dto.ResponseDTO;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * saml登录失败Handler
 */
@Component
public class SamlAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        ServletUtil.write(response, JSONUtil.toJsonStr(new ResponseDTO<>(false, "400")), ContentType.JSON.toString());
    }
}
