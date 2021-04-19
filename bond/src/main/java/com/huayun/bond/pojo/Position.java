package com.huayun.bond.pojo;

import lombok.Data;

@Data
public class Position {
    private String accountID;//证券帐户
    private String securityID;//证券代码
    private Long quantity;//实时持仓数量
    private Long originQty;//当天前的原始持仓的数量
    private Double originOpenPrice;//当天前的原始持仓的平均开仓价格
    private Long freeQty;//当前可卖持仓数量
    private Long frozenQty;//持仓冻结数量
    private Double price;//平均买入价格
    private Double profitAndLoss;//盈亏
    private Double rateOfReturn;//收益率
    private Long createTime;
    private Long updateTime;
}
