package cn.dooling.samlazure.dao;

import cn.dooling.samlazure.domain.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserRoleDao {
    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    List<SysUserRole> selectByUserId(Integer userId);
}