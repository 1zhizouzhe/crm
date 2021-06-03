package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;
import com.xxxx.crm.vo.Role;

public class RoleQuery extends BaseQuery{

    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
