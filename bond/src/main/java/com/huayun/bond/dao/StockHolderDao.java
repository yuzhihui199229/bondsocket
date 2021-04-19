package com.huayun.bond.dao;

import com.huayun.bond.pojo.StockHolder;

import java.util.List;

public interface StockHolderDao {
    List<StockHolder> qryStockHolderInfo(StockHolder stockHolder);

    int qryStockHolderInfoCount(StockHolder stockHolder);
}
