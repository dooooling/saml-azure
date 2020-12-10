package cn.dooling.samlazure.config.saml;

import cn.dooling.samlazure.module.user.domain.entity.SysRole;
import cn.dooling.samlazure.module.user.domain.entity.SysUser;
import cn.dooling.samlazure.module.user.domain.entity.SysUserRole;
import cn.dooling.samlazure.module.user.service.RoleService;
import cn.dooling.samlazure.module.user.service.UserRoleService;
import cn.dooling.samlazure.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml2.provider.service.authentication.OpenSamlAuthenticationProvider;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class SamlAuthenticationProvider implements AuthenticationProvider {

    public OpenSamlAuthenticationProvider provider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;


    public SamlAuthenticationProvider() {
        this.provider = new OpenSamlAuthenticationProvider();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        authentication = provider.authenticate(authentication);
        Saml2Authentication saml2Authentication = (Saml2Authentication) authentication;
        SysUser sysUser = userService.selectByName(authentication.getName());
        return Optional.ofNullable(sysUser).map((Function<SysUser, Authentication>) sysUser1 -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            //添加用户权限
            List<SysUserRole> sysUserRoles = userRoleService.listByUserId(sysUser1.getId());
            Optional.ofNullable(sysUserRoles).ifPresent(roles -> roles.forEach(sysUserRole -> {
                SysRole sysRole = roleService.selectById(sysUserRole.getRoleId());
                Optional.ofNullable(sysRole).ifPresent(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
            }));
            return new Saml2Authentication((AuthenticatedPrincipal) saml2Authentication.getPrincipal(), saml2Authentication.getSaml2Response(), authorities);
        }).orElseThrow((Supplier<RuntimeException>) () -> new UsernameNotFoundException("用户名不存在"));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return provider.supports(authentication);
    }
}
