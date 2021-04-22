package com.huayun.bond.dao;

import com.huayun.bond.pojo.Deal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DealDao {
    List<Deal> qryDeal(@Param("clOrdID") String ClOrdID,@Param("execID")String execID,@Param("offset") int offset,@Param("row") int row,@Param("startTime") Long startTime,@Param("endTime") Long endTime);

    int qryDealCount(@Param("clOrdID") String ClOrdID,@Param("execID")String execID,@Param("startTime") Long startTime,@Param("endTime") Long endTime);
}
