package cn.dooling.samlazure.service;

import cn.dooling.samlazure.domain.entity.SysUserRole;

import java.util.List;

public interface UserRoleService {
    List<SysUserRole> listByUserId(Integer userId);
}
