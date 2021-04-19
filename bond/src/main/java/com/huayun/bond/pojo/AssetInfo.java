package com.huayun.bond.pojo;

import lombok.Data;

@Data
public class AssetInfo {
    private String userId;//用户id
    private String assetAccount;//资金账户
    private Double balance;//余额
    private Double frozen;//冻结资金
    private Long createTime;//注册时间
    private Long updateTime;//修改时间
}
