package com.huayun.bond.dao;

import com.huayun.bond.pojo.Order;
import com.huayun.bond.pojo.RecallOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RecallOrderDao {
    List<RecallOrder> qryRecallOrder(@Param("clOrdID") String clOrdID,@Param("secufityID")String secufityID, @Param("offset") int offset, @Param("row") int row,@Param("startTime") Long startTime,@Param("endTime") Long endTime);

    int qryRecallOrderCount(@Param("clOrdID") String clOrdID,@Param("secufityID")String secufityID,@Param("startTime") Long startTime,@Param("endTime") Long endTime);
}
