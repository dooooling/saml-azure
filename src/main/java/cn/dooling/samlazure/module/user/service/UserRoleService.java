package cn.dooling.samlazure.module.user.service;

import cn.dooling.samlazure.module.user.domain.entity.SysUserRole;

import java.util.List;

public interface UserRoleService {
    List<SysUserRole> listByUserId(Integer userId);
}
