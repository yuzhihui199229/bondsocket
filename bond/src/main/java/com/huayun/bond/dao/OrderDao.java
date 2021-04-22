package com.huayun.bond.dao;

import com.huayun.bond.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDao {
    List<Order> qryOrder(@Param("clOrdID") String clOrdID,@Param("secufityID")String secufityID,@Param("offset") int offset,@Param("row") int row,@Param("startTime") Long startTime,@Param("endTime") Long endTime);

    int qryOrderCount(@Param("clOrdID") String clOrdID,@Param("secufityID")String secufityID,@Param("startTime") Long startTime,@Param("endTime") Long endTime);
}
