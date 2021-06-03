package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.service.PermissionService;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {
    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;


    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("main")
    public String main(HttpServletRequest req){
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
        User user = userService.selectByPrimaryKey(userId);
        req.setAttribute("user",user);
        List<String> permissions = permissionService.queryUserHasRolesHasPermission(userId);
        req.getSession().setAttribute("permissions",permissions);
        return "main";
    }

    /**
     * 实现页面跳转
     * @return
     */
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }


    /**
     * 后端管理主页面
     * @param req
     * @return
     */
    @RequestMapping("main2")
    public String main2(HttpServletRequest req){
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
        req.setAttribute("user",userService.selectByPrimaryKey(userId));
        List<String> permissions = permissionService.queryUserHasRolesHasPermission(userId);
        req.getSession().setAttribute("permissions",permissions);
        return "main";
    }
}
