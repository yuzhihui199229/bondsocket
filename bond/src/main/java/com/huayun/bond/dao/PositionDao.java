package com.huayun.bond.dao;

import com.huayun.bond.pojo.Position;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PositionDao {
    List<Position> qryPosition(Position position, @Param("offset") int offset,@Param("row") int row,@Param("startTime") Long startTime,@Param("endTime") Long endTime);

    int qryPositionCount(Position position,@Param("startTime") Long startTime,@Param("endTime") Long endTime);
}
