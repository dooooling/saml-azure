package cn.dooling.samlazure.module.user.domain.entity;

import java.io.Serializable;

/**
 * sys_role
 * @author 
 */
public class SysRole implements Serializable {
    private Integer id;

    private String name;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}