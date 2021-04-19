package com.huayun.bond.pojo;

import lombok.Data;

@Data
public class Order {
    private String submittingPBUID;//申报交易单元
    private String securityID;//证券代码
    private String securityIDSource;//证券代码源
    private Short ownerType;//订单所有者类型
    private String clearingFirm;//结算机构代码
    private Long transactTime;//委托时间
    private String userInfo;//用户私有信息
    private String clOrdID;//客户订单编号
    private String accountID;//证券帐户
    private String branchID;//营业部代码
    private String orderRestrictions;//订单限定
    private Byte side;//买卖方向
    private Byte ordType;//订单类别
    private Long orderQty;//订单数量
    private Long price;//价格
    private Long stopPx;//止损价
    private Long minQty;//最低成交数量
    private Short maxPriceLevels;//最多成交价位数
    private Byte timeInforce;//订单有效时间类型
    private Byte cashMargin;//合约帐户标识码
    private Byte ordStatus;//订单状态
}