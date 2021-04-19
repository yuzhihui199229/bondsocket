package com.huayun.bond.dao;

import com.huayun.bond.pojo.TradeTimeInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TradeTimeGroupDao {
    List<TradeTimeInfo> qryTradeTimeGroup(@Param("tradeTimeGroup") String tradeTimeGroup);

    int qryCount(@Param("tradeTimeGroup") String tradeTimeGroup);
}
