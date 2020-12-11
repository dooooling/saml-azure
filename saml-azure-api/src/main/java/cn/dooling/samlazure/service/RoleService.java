package cn.dooling.samlazure.service;

import cn.dooling.samlazure.domain.entity.SysRole;

public interface RoleService {
    SysRole selectById(Integer id);
}
