package com.xxxx.crm.controller;

import com.xxxx.crm.Model.UserModel;
import com.xxxx.crm.annotation.RequirePermission;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.ParamsException;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.SaleChance;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    /**
     * 用户登录
     * 调用userService实现业务
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = userService.userLogin(userName, userPwd);
        resultInfo.setResult(userModel);

        /*try {


        }catch (ParamsException pe){
            pe.printStackTrace();
            resultInfo.setMsg(pe.getMsg());
            resultInfo.setCode(pe.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("操作失败");
        }*/

        return resultInfo;
    }

    @PostMapping("updatePassword")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest req,String oldPassword,String newPassword,String confirmPassword){
        ResultInfo resultInfo = new ResultInfo();

        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        userService.updateUserPassword(userId, oldPassword, newPassword, confirmPassword);

        /*try {
        }catch (ParamsException pe){
            pe.printStackTrace();
            resultInfo.setCode(pe.getCode());
            resultInfo.setMsg(pe.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(300);
            resultInfo.setMsg("操作失败");
        }*/
        return resultInfo;
    }

    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    @RequirePermission(code = "10")
    public List<Map<String, Object>> queryAllSales() {
        return userService.queryAllSales();
    }

    /**
     * 多条件查询用户数据
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    @RequirePermission(code = "6010")
    public Map<String, Object> queryUserByParams(UserQuery userQuery) {
        return userService.queryUserByParams(userQuery);
    }

    /**
     * 进入用户页面
     * @return
     */
    @RequestMapping("index")
    @RequirePermission(code = "60")
    public String index(){
        return "user/user";
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    @RequirePermission(code = "6010")
    public ResultInfo saveUser(User user) {
        userService.saveUser(user);
        return success("用户添加成功！");
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    @RequirePermission(code = "6010")
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success("用户更新成功！");
    }

    /**
     * 进入用户添加或更新页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateUserPage")
    @RequirePermission(code = "6010")
    public String addUserPage(Integer id, Model model){
        if(null != id){
            model.addAttribute("user",userService.selectByPrimaryKey(id));
        }
        return "user/add_update";
    }

    @RequestMapping("delete")
    @ResponseBody
    @RequirePermission(code = "6010")
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteUser(ids);
        return success("删除成功");
    }
}
