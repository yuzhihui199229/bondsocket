package com.huayun.bond.pojo;

import lombok.Data;

@Data
public class Security {
    private String securityID;//证券代码
    private String securityIDSource;//证券代码源
    private String securityName;//证券名称
    private Short status;//证券状态（0：正常 1：停盘 2：退市）
    private Integer minQty;//最小数量
    private Integer maxQty;//最大数量
    private Double feeRate;//手续费
    private String tradeTimeGroup;//交易时间组
    private Long updateTime;//更新时间
}
