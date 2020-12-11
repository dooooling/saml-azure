package cn.dooling.samlazure.config.saml;

import cn.dooling.samlazure.domain.entity.SysRole;
import cn.dooling.samlazure.domain.entity.SysUser;
import cn.dooling.samlazure.domain.entity.SysUserRole;
import cn.dooling.samlazure.service.RoleService;
import cn.dooling.samlazure.service.UserRoleService;
import cn.dooling.samlazure.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        SysUser sysUser = userService.selectByName(username);
        return Optional.ofNullable(sysUser).map((Function<SysUser, UserDetails>) user -> {
            //添加用户权限
            List<SysUserRole> sysUserRoles = userRoleService.listByUserId(user.getId());
            Optional.ofNullable(sysUserRoles).orElse(Collections.emptyList()).forEach(sysUserRole -> {
                SysRole sysRole = roleService.selectById(sysUserRole.getRoleId());
                Optional.ofNullable(sysRole).ifPresent(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
            });

            return new User(sysUser.getName(), sysUser.getPassword(), authorities);
        }).orElseThrow((Supplier<RuntimeException>) () -> new UsernameNotFoundException("用户名未找到"));
    }
}
