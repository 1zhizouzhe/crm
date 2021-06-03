package com.xxxx.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.Model.UserModel;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.dao.UserRoleMapper;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import com.xxxx.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String confirmPassword){

        User user = userMapper.selectByPrimaryKey(userId);
        checkPasswordParams(user,oldPassword,newPassword,confirmPassword);
        user.setUserPwd(Md5Util.encode(newPassword));
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"用户密码更新失败");

    }

    /**
     * 修改密码时确认三个密码是否为空是否相等
     * @param user
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    private void checkPasswordParams(User user, String oldPassword, String newPassword, String confirmPassword) {
        AssertUtil.isTrue(null==user,"用户未登录或不存在");
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码");
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))),"原始密码不正确");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"请输入新密码");
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码不能与原始密码相同");
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"请确认新密码");
        AssertUtil.isTrue(!(confirmPassword.equals(newPassword)),"两遍密码不统一");

    }

    public UserModel userLogin(String userName,String userPwd){
        //验证用户名和密码不能为空
        checkUserParam(userName,userPwd);
        //查询用户
        User user = userMapper.selectUserByUserName(userName);
        //判断数据库中有没有
        AssertUtil.isTrue(null==user,"用户不存在或已经注销");
        //判断密码的正确与否
        checkLoginPwd(userPwd,user.getUserPwd());
        //返回用户模型
        return builderUserInfo(user);
    }

    /**
     * 用正确的用户名和密码创建用户模型
     * @param user
     * @return
     */
    private UserModel builderUserInfo(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 比较数据库中的密码和输入的密码是否匹配
     * 需要进行解码
     * @param inputPwd
     * @param dbPwd
     */
    private void checkLoginPwd(String inputPwd, String dbPwd) {
        //数据库中的密码解码
        inputPwd = Md5Util.encode(inputPwd);
        AssertUtil.isTrue(!inputPwd.equals(dbPwd),"用户密码不正确");
    }

    /**
     * 检验用户名和密码是否为空
     * @param userName
     * @param userPwd
     */
    private void checkUserParam(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户名不能为空");
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    public List<Map<String, Object>> queryAllSales() {
        return userMapper.queryAllSales();
    }


    /**
     * 多条件分页查询用户数据
     * @param query
     * @return
     */
    public Map<String, Object> queryUserByParams (UserQuery query) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(query.getPage(), query.getLimit());
        PageInfo<User> pageInfo = new PageInfo<>(userMapper.selectByParams(query));
        map.put("code",0);
        map.put("msg", "");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 添加用户
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user) {
        // 1. 参数校验
        checkParams(user.getUserName(), user.getEmail(), user.getPhone());
        // 2. 设置默认参数
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        // 3. 执行添加，判断结果
        AssertUtil.isTrue(userMapper.insertSelective(user) == null, "用户添加失败！");

        relationUserRole(user.getId(),user.getRoleIds());

        System.out.println("插入数据");
    }

    /**
     * 参数校验
     * @param userName
     * @param email
     * @param phone
     */
    private void checkParams(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空！");
        // 验证用户名是否存在
        User temp = userMapper.queryUserByUserName(userName);
        AssertUtil.isTrue(null != temp, "该用户已存在！");
        AssertUtil.isTrue(StringUtils.isBlank(email), "请输入邮箱地址！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone), "手机号码格式不正确！");
    }

    /**
     * 更新用户
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        // 1. 参数校验
        // 通过id查询用户对象
        User temp = userMapper.selectByPrimaryKey(user.getId());
        // 判断对象是否存在
        AssertUtil.isTrue(temp == null, "待更新记录不存在！");
        // 验证参数
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()), "用户名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(user.getEmail()), "请输入邮箱地址！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()), "手机号码格式不正确！");
        // 2. 设置默认参数
        user.setUpdateDate(new Date());
        // 3. 执行更新，判断结果
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户更新失败！");

        Integer userId = userMapper.queryUserByUserName(user.getUserName()).getId();
        relationUserRole(userId,user.getRoleIds());
        System.out.println("更新数据");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Integer[] ids){
        AssertUtil.isTrue(ids==null || ids.length<1,"数据不存在");
        AssertUtil.isTrue(deleteBatch(ids)<1,"删除失败");
    }

    private void relationUserRole(Integer userId,String roleIds){
        int count = userRoleMapper.countUserROleByUserId(userId);

        //System.out.println("11111111111");
        if(count > 0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count,"用户角色分配失败");
        }
        //System.out.println("22222222222");
        if(StringUtils.isNotBlank(roleIds)){
            List<UserRole> userRoles = new ArrayList<>();
            for(String s:roleIds.split(",")){
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(Integer.parseInt(s));
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoles.add(userRole);
                //System.out.println("33333333333");
            }
            //System.out.println("4444444444");
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoles)<userRoles.size(),"用户角色分配失败");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser02(Integer userId){
        User user = selectByPrimaryKey(userId);
        AssertUtil.isTrue(null == userId || null == user,"待删除记录不存在！");
        int count = userRoleMapper.countUserROleByUserId(userId);
        if(count > 0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count,"用户角色删除失败");
        }
        user.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"用户记录删除失败");
    }
}
