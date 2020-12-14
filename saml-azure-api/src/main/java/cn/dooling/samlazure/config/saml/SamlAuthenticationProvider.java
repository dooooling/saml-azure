package cn.dooling.samlazure.config.saml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.saml2.provider.service.authentication.OpenSamlAuthenticationProvider;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.stereotype.Component;

/**
 * saml身份验证提供
 */
@Component
public class SamlAuthenticationProvider implements AuthenticationProvider {

    public OpenSamlAuthenticationProvider provider;

    @Autowired
    private UserDetailsService userDetailsService;


    public SamlAuthenticationProvider() {
        this.provider = new OpenSamlAuthenticationProvider();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        authentication = provider.authenticate(authentication);
        Saml2Authentication saml2Authentication = (Saml2Authentication) authentication;
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        return new UsernamePasswordAuthenticationToken(userDetails, saml2Authentication.getSaml2Response(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return provider.supports(authentication);
    }
}
