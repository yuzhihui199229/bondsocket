package com.huayun.bond.pojo;

import lombok.Data;

@Data
public class RecallOrderSuccess {
    private Byte ordType;//订单类别
    private Long orderQty;//订单数量
    private Long price;//价格
    private String orderRestrictions;//订单限定
    private Long minQty;//最低成交数量
    private Short maxPriceLevels;//最多成交价位数
    private Byte timeInforce;//订单有效时间类型
    private String origClOrdId;//原始订单客户订单编号
    private String orderId;//
}
