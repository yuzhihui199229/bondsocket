package com.huayun.bond.dao;

import com.huayun.bond.pojo.AssetInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AssetInfoDao {
    List<AssetInfo> qryAssetInfo(@Param("userId") String userId);

    int qryAssetInfoCount(@Param("userId") String userId);
}
