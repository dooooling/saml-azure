package cn.dooling.samlazure.service.impl;

import cn.dooling.samlazure.dao.SysUserDao;
import cn.dooling.samlazure.domain.entity.SysUser;
import cn.dooling.samlazure.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public SysUser selectById(Integer id) {
        return sysUserDao.selectByPrimaryKey(id);
    }

    @Override
    public SysUser selectByName(String name) {
        return sysUserDao.selectByName(name);
    }
}
