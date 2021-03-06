package com.huayun.bond.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfo implements Serializable {
    private String userId;
    private String userName;
    private String companyId;
    private String userpassword;
    private String phone;
    private String userAccount;
    private Character userPropty;
    private Character status;
    private Long createTime;
    private Long updateTime;
}
