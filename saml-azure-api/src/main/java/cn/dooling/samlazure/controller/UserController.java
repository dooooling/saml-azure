package cn.dooling.samlazure.controller;

import cn.dooling.samlazure.domain.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/info")
    public ResponseDTO<Object> subject(Authentication authentication, Principal principal) {
        return new ResponseDTO<>(userDetailsService.loadUserByUsername(principal.getName()));
    }
}
