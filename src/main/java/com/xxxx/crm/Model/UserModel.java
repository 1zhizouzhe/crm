package com.xxxx.crm.Model;


/**
 * userModel用来返回登录成功后的用户信息
 * 用户Id
 * 用户名
 * 真实用户名
 */
public class UserModel {
    private String userIdStr;
    private String userName;
    private String trueName;

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}
