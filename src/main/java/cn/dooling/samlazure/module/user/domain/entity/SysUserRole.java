package cn.dooling.samlazure.module.user.domain.entity;

import java.io.Serializable;

/**
 * sys_user_role
 * @author 
 */
public class SysUserRole implements Serializable {
    private Integer userId;

    private Integer roleId;

    private Integer id;

    private static final long serialVersionUID = 1L;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}