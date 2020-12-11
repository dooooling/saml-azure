package cn.dooling.samlazure.service;

import cn.dooling.samlazure.domain.entity.SysUser;

public interface UserService {
    SysUser selectById(Integer id);

    SysUser selectByName(String name);
}
