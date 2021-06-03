package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {

    public List<Map<String,Object>> selectAllRoles(Integer userId);

    public List<Role> selectByQuery(RoleQuery roleQuery);

    Role selectRoleByRoleName(String roleName);

    public Integer deleteById(Integer id);

    List<Map<String,Object>> queryAllRoles(Integer userId);
}