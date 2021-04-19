package com.huayun.bond.pojo;

import lombok.Data;

import java.awt.print.PrinterAbortException;

@Data
public class RecallOrder {
    private String submittingPBUID;//申报交易单元
    private String securityID;//证券代码
    private String securityIDSource;//证券代码源
    private Short ownerType ;//订单所有者类
    private String clearingFirm ;//结算机构代码
    private Long transactTime ;//委托时间
    private String userInfo ;//用户私有信息
    private String clOrdID  ;//客户订单编号
    private String origClOrdID  ;//原始订单客户订单编号
    private Byte side  ;//买卖方向
    private String orderID  ;//原始订单交易所订单编号
    private Long orderQty  ;//原始订单数量
    private Byte ordStatus  ;//订单状态
    private Order order;//订单
}