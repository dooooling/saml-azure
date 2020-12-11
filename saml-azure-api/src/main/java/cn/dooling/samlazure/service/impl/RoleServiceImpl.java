package cn.dooling.samlazure.service.impl;

import cn.dooling.samlazure.dao.SysRoleDao;
import cn.dooling.samlazure.domain.entity.SysRole;
import cn.dooling.samlazure.service.RoleService;
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
