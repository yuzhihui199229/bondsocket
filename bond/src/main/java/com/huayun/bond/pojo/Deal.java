package com.huayun.bond.pojo;

import lombok.Data;

@Data
public class Deal {
    private Integer partitionNo;//平台分区号
    private Long reportIndex;//回报记录号
    private String applID;//应用标识
    private String reportingPBUID;//回报交易单元
    private String submittingPBUID;//申报交易单元
    private String securityID;//证券代码
    private String securityIDSource;//证券代码源
    private Short ownerType;//订单所有者类型
    private String clearingFirm;//结算机构代码
    private Long transactTime;//委托时间
    private String userInfo;//用户私有信息
    private String orderID;//交易所订单编号
    private String clOrdID;//客户订单编号
    private String execID;//执行编号
    private Byte execType;//执行类型
    private Byte ordStatus;//订单状态
    private Long lastPx;//成交价
    private Long lastQty;//成交数量
    private Long leavesQty;//订单剩余数量
    private Long cumQty;//累计执行数量
    private Byte side;//买卖方向
    private String accountID;//证券帐户
    private String branchID;//营业部代码
    private Byte cashMargin;//信用标识
    private RecallOrderSuccess recallOrderSuccess;
}
