package com.huayun.bond.service;

import com.huayun.bond.dao.StockHolderDao;
import com.huayun.bond.pojo.MessageProtocol;
import com.huayun.bond.pojo.ResponseMsg;
import com.huayun.bond.pojo.StockHolder;
import com.huayun.bond.util.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 股东
 */
@Service
public class StockHolderService {
    @Autowired
    private StockHolderDao stockHolderDao;

    public MessageProtocol qryStockHolderInfo(MessageProtocol msg) throws UnsupportedEncodingException {
        byte[] buf = msg.getContent();
        //转化为userid
        byte[] userByte = new byte[13];
        System.arraycopy(buf, 32, userByte, 0, 12);
        String userId = ByteUtil.getString(userByte);
        //转化为accountId
        byte[] accountIdByte = new byte[33];
        System.arraycopy(buf, 44, accountIdByte, 0, 32);
        String accountId = ByteUtil.getString(accountIdByte);
        StockHolder stockHolder = new StockHolder();
        stockHolder.setUserId(userId);
        stockHolder.setAccountId(accountId);
        List<StockHolder> stockHolders = stockHolderDao.qryStockHolderInfo(stockHolder);
        int count = stockHolderDao.qryStockHolderInfoCount(stockHolder);
        int i = 0;
        byte[] stockHolderBytes = new byte[64 * stockHolders.size()];
        for (StockHolder holder : stockHolders) {
            ++i;
            byte[] stockHolderByte = new byte[64];
            String userId1 = holder.getUserId();
            if (userId1 != null) {
                byte[] bytes = userId1.getBytes("UTF-8");
                System.arraycopy(bytes, 0, stockHolderByte,0, bytes.length);
            }
            String accountId1 = holder.getAccountId();
            if (accountId1 != null) {
                byte[] bytes = accountId1.getBytes("UTF-8");
                System.arraycopy(bytes, 0, stockHolderByte, 12, bytes.length);
            }
            Short accountType = holder.getAccountType();
            if (accountType != null) {
                byte[] bytes = ByteUtil.getBytes(accountType);
                System.arraycopy(bytes, 0, stockHolderByte, 44, bytes.length);
            }
            Short status = holder.getStatus();
            if (status != null) {
                byte[] bytes = ByteUtil.getBytes(status);
                System.arraycopy(bytes, 0, stockHolderByte, 46, bytes.length);
            }
            Long createTime = holder.getCreateTime();
            if (createTime != null) {
                byte[] bytes = ByteUtil.getBytes(createTime);
                System.arraycopy(bytes, 0, stockHolderByte, 48, bytes.length);
            }
            Long updateTime = holder.getUpdateTime();
            if (updateTime != null) {
                byte[] bytes = ByteUtil.getBytes(updateTime);
                System.arraycopy(bytes, 0, stockHolderByte, 56, bytes.length);
            }
            System.arraycopy(stockHolderByte, 0, stockHolderBytes, (i - 1) * 64, 64);
        }
        MessageProtocol result = msg;
        byte[] content = new byte[36 + 64 * stockHolders.size()];
        String comment = ResponseMsg.OK.getMsg();
        byte[] commentBytes = comment.getBytes();
        System.arraycopy(commentBytes, 0, content, 0, commentBytes.length);
        byte[] countBytes = ByteUtil.getBytes(count);
        System.arraycopy(countBytes, 0, content, 32, countBytes.length);
        System.arraycopy(stockHolderBytes, 0, content, 36, stockHolderBytes.length);
        msg.setLen(68+64 * stockHolders.size());
        msg.setUiRetCode(ResponseMsg.OK.getRetCode());
        result.setSzMagicNum(msg.getSzMagicNum());
        result.setByVersion(msg.getByVersion());
        result.setByMsgType((byte) 2);
        result.setUiSourceID(msg.getUiSourceID());
        result.setUiSessionID(msg.getUiSessionID());
        result.setUiFuncNo(msg.getUiFuncNo());
        result.setUiMsgSeq(msg.getUiMsgSeq());
        result.setUiMktCode(msg.getUiMktCode());
        result.setByReserved(msg.getByReserved());
        result.setContent(content);
        return result;
    }
}
