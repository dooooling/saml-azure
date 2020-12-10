package cn.dooling.samlazure.module.user.service.impl;

import cn.dooling.samlazure.module.user.dao.SysRoleDao;
import cn.dooling.samlazure.module.user.domain.entity.SysRole;
import cn.dooling.samlazure.module.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleDao sysRoleDao;

    @Override
    public SysRole selectById(Integer id) {
        return sysRoleDao.selectByPrimaryKey(id);
    }
}
