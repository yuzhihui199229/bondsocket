package com.huayun.bond.pojo;

import lombok.Data;

/**
 * TB_STOCKHOLDERINFO
 */
@Data
public class StockHolder {
    private String userId;//用户ID
    private String accountId;//股东账户
    private Short accountType;//账户类型（0：股票）
    private Short status;//账户状态（0：正常 1：冻结）
    private Long createTime;//注册时间
    private Long updateTime;//更新时间
}
