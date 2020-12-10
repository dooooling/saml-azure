package cn.dooling.samlazure.module.user.service;

import cn.dooling.samlazure.module.user.domain.entity.SysRole;

public interface RoleService {
    SysRole selectById(Integer id);
}
