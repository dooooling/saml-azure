package cn.dooling.samlazure.config.jwt;

import cn.dooling.samlazure.domain.dto.LoginFormDTO;
import cn.dooling.samlazure.domain.dto.ResponseDTO;
import cn.dooling.samlazure.helper.JWTHelper;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JWTHelper jwtHelper;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
        setAuthenticationManager(authenticationManager);
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/auth/login", "POST"));
        setAuthenticationSuccessHandler((request, response, authentication) -> ServletUtil.write(response, JSONUtil.toJsonStr(new ResponseDTO<>(jwtHelper.getToken(response))), ContentType.JSON.toString()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        // 从输入流中获取到登录的信息
        try {
            LoginFormDTO loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginFormDTO.class);
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword());
            setDetails(request, authRequest);
            return getAuthenticationManager()
                    .authenticate(authRequest);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        //写入token到response
        jwtHelper.createTokenToHeader(userDetails.getUsername(), userDetails.getPassword(), response);
        super.successfulAuthentication(request, response, chain, authResult);
    }
}

