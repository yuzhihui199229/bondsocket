package com.huayun.bond.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TellerInfo implements Serializable {
    private String userId;
    private String userName;
    private String userpassword;
    private Byte clientType;
    private Byte status;
    private String roleId;
    private Long createTime;
    private Long updateTime;
}
