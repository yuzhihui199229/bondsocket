package com.huayun.bond.pojo;

import lombok.Data;

@Data
public class TradeTimeInfo {
    private Integer id;
    private String tradeTimeGroup;
    private Integer startTime;
    private Integer stopTime;
    private Long createTime;
    private Long updateTime;
}
