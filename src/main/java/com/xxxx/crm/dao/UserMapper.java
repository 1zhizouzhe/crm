package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {
    Integer deleteByPrimaryKey(Integer id);

    int insert(User record);

    Integer insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    Integer updateByPrimaryKeySelective(User record);

    Integer updateByPrimaryKey(User record);

    User selectUserByUserName(String userName);

    /**
     * 查询所有的销售人员
     * @return
     */
    // 查询所有的销售人员
    public List<Map<String, Object>> queryAllSales();

    public User queryUserByUserName(String userName);
}