package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Permission;
import com.xxxx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService extends BaseService<Role,Integer> {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 查询所有的角色
     * @return
     */
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }


    /**
     * 分页查询所有角色
     * @param roleQuery
     * @return
     */
    public Map<String,Object> queryAllByQuery(RoleQuery roleQuery){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getLimit());
        PageInfo<Role> pageInfo = new PageInfo<>(roleMapper.selectByQuery(roleQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名称");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleRemark()),"请输入角色的备注");
        Role temp = roleMapper.selectRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null != temp,"角色已存在");
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        role.setIsValid(1);
        AssertUtil.isTrue(roleMapper.insertSelective(role)<1,"新角色添加失败");
    }


    /**
     * 更新角色记录
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        AssertUtil.isTrue(null == role.getId() || null == selectByPrimaryKey(role.getId()),"待修改记录不存在");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名称");
        Role temp = roleMapper.selectRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null != temp,"角色已存在");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(role)<1,"更新失败");
    }

    /**
     * 实现数据的删除
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer id){
        AssertUtil.isTrue(null == id,"请选择要删除的数据");
        AssertUtil.isTrue(roleMapper.deleteById(id)<1,"角色删除失败");
    }

    public void addGrant(Integer[] mids,Integer roleId){
        Role temp = selectByPrimaryKey(roleId);
        AssertUtil.isTrue(null == roleId || null == temp,"待授权的角色不存在");
        int count = permissionMapper.countPermissionByRoleId(roleId);
        if(count>0){
            AssertUtil.isTrue(permissionMapper.deletePermissionByRoleId(roleId)<count,"权限分配失败");
        }
        if(null != mids && mids.length>0){
            List<Permission> permissions = new ArrayList<Permission>();
            for(Integer mid : mids){
                Permission permission = new Permission();
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setModuleId(mid);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                permissions.add(permission);
            }
            permissionMapper.insertBatch(permissions);
        }
    }
}
