package com.huayun.bond.dao;

import com.huayun.bond.pojo.Asset;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AssetDao {
    List<Asset> qryAsset(Asset asset, @Param("offset") int offset, @Param("row") int row,@Param("userId")String userId,@Param("startTime")Long startTime,@Param("endTime")Long endTime);

    int qryAssetCount(Asset asset,@Param("userId")String userId,@Param("startTime")Long startTime,@Param("endTime")Long endTime);
}
