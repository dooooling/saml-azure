package cn.dooling.samlazure.module.user.service;

import cn.dooling.samlazure.module.user.domain.entity.SysUser;

public interface UserService {
    SysUser selectById(Integer id);

    SysUser selectByName(String name);
}
