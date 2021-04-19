package com.huayun.bond.pojo;

import lombok.Data;

@Data
public class Asset {
    private Integer id;//id
    private String securityId;//证券代码
    private String securityIdSource;//证券代码源
    private String accountId;//证券账户
    private Integer holdId;//持仓ID
    private String execId;//执行编号
    private Double fromBalance;//成交前余额
    private Double fromFronze;//成交前冻结
    private Double toBalance;//成交后余额
    private Double toFronze;//成交后冻结
    private Long createTime;//创建时间
    private StockHolder stockHolder;
}
