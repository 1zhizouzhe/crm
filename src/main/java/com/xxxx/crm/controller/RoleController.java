package com.xxxx.crm.controller;

import com.xxxx.crm.annotation.RequirePermission;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.service.RoleService;
import com.xxxx.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 查询所有的角色
     * @return
     */
    @RequestMapping("queryAll")
    @ResponseBody
    @RequirePermission(code = "6020")
    public List<Map<String,Object>> queryAllRoles(Integer userId){

        return roleService.queryAllRoles(userId);
    }

    /**
     * 点击角色管理实现页面跳转
     * @return
     */
    @RequestMapping("index")
    @RequirePermission(code = "60")
    public String index(){
        return "role/role";
    }

    @RequestMapping("list")
    @ResponseBody
    @RequirePermission(code = "6020")
    public Map<String,Object> queryAll(RoleQuery roleQuery){

        return roleService.queryAllByQuery(roleQuery);
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    @RequirePermission(code = "6020")
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success("添加成功");
    }

    /**
     * 更新成功
     * @param role
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    @RequirePermission(code = "6020")
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("更新成功");
    }

    /**
     * 点击按钮，实现页面跳转
     * @return
     */
    @RequestMapping("addOrUpdateRolePage")
    @RequirePermission(code = "6020")
    public String addOrUpdateRolePage(Integer id, Model model){

        if(null != id){
            model.addAttribute("role",roleService.selectByPrimaryKey(id));
        }
        return "role/add_update";
    }

    /**
     * 实现角色删除（单条记录删除）
     * @param id
     * @return
     */
    @RequestMapping("del")
    @ResponseBody
    @RequirePermission(code = "6020")
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("角色删除成功");
    }

    /**
     * 实现授权页面的跳转
     * @param roleId
     * @param model
     * @return
     */
    @RequestMapping("toAddGrantPage")
    @RequirePermission(code = "6020")
    public String toAddGrantPage(Integer roleId, Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

    /**
     * 权限添加
     * @param mids
     * @param roleId
     * @return
     */
    @RequestMapping("addGrant")
    @ResponseBody
    @RequirePermission(code = "6020")
    public ResultInfo addGrant(Integer[] mids,Integer roleId){
        roleService.addGrant(mids,roleId);
        return success("权限添加成功");
    }
}
