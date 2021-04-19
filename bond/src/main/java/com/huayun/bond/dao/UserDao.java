package com.huayun.bond.dao;

import com.huayun.bond.pojo.UserInfo;

import java.util.List;

public interface UserDao {
    List<UserInfo> qryUserInfo(UserInfo userInfo);

    int qryUserInfoCount(UserInfo userInfo);
}
