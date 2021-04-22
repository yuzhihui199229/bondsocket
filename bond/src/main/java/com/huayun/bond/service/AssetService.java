package com.huayun.bond.service;

import com.huayun.bond.dao.AssetDao;
import com.huayun.bond.pojo.*;
import com.huayun.bond.util.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 客户资金流水查询接口
 */
@Service
public class AssetService {
    @Autowired
    private AssetDao assetDao;

    public MessageProtocol qryAsset(MessageProtocol msg) throws UnsupportedEncodingException {
        byte[] buf = msg.getContent();
        byte[] accountIdBytes = new byte[13];
        System.arraycopy(buf, 32, accountIdBytes, 0, 12);
        String accountId = ByteUtil.getString(accountIdBytes);
        byte[] securityIDBytes = new byte[9];
        System.arraycopy(buf, 44, securityIDBytes, 0, 8);
        String securityIdt = ByteUtil.getString(securityIDBytes);
        byte[] execIdBytes = new byte[17];
        System.arraycopy(buf, 52, execIdBytes, 0, 16);
        String execIdi = ByteUtil.getString(execIdBytes);
        byte[] userIdBytes = new byte[13];
        System.arraycopy(buf, 68, userIdBytes, 0, 12);
        String userIdi = ByteUtil.getString(userIdBytes);
        byte[] idBytes=new byte[4];
        System.arraycopy(buf,80,idBytes,0,4);
        int idi = ByteUtil.getInt(idBytes);
        byte[] pageBytes = new byte[4];
        System.arraycopy(buf, 84, pageBytes, 0, 4);
        int page = ByteUtil.getInt(pageBytes);
        byte[] numBytes = new byte[4];
        System.arraycopy(buf, 88, numBytes, 0, 4);
        int num = ByteUtil.getInt(numBytes);
        byte[] startTimeBytes = new byte[8];
        System.arraycopy(buf, 92, startTimeBytes, 0, 8);
        long startTime = ByteUtil.getLong(startTimeBytes);
        byte[] endTimeBytes = new byte[8];
        System.arraycopy(buf, 100, endTimeBytes, 0, 8);
        long endTime = ByteUtil.getLong(endTimeBytes);
        int offset = (page - 1) * num;
        Asset asset = new Asset();
        asset.setAccountId(accountId);
        asset.setSecurityId(securityIdt);
        asset.setId(idi);
        asset.setExecId(execIdi);
        List<Asset> assets = assetDao.qryAsset(asset, offset, num, userIdi, startTime, endTime);
        int count = assetDao.qryAssetCount(asset, userIdi, startTime, endTime);
        byte[] assetBytes = new byte[100 * assets.size()];
        int i = 0;
        for (Asset asset1 : assets) {
            ++i;
            byte[] assetByte = new byte[100];
            Integer id = asset1.getId();
            if (id != null) {
                byte[] bytes = ByteUtil.getBytes(id);
                System.arraycopy(bytes, 0, assetByte, 0, bytes.length);
            }
            String securityId = asset1.getSecurityId();
            if (securityId != null) {
                byte[] bytes = securityId.getBytes("UTF-8");
                System.arraycopy(bytes, 0, assetByte, 4, bytes.length);
            }
            String securityIdSource = asset1.getSecurityIdSource();
            if (securityIdSource != null) {
                byte[] bytes = securityIdSource.getBytes("UTF-8");
                System.arraycopy(bytes, 0, assetByte, 12, bytes.length);
            }
            String accountId1 = asset1.getAccountId();
            if (accountId1 != null) {
                byte[] bytes = accountId1.getBytes("UTF-8");
                System.arraycopy(bytes, 0, assetByte, 16, bytes.length);
            }
            Integer holdId1 = asset1.getHoldId();
            if (holdId1 != null) {
                byte[] bytes = ByteUtil.getBytes(holdId1);
                System.arraycopy(bytes, 0, assetByte, 28, bytes.length);
            }
            String execId = asset1.getExecId();
            if (execId != null) {
                byte[] bytes = execId.getBytes("UTF-8");
                System.arraycopy(bytes, 0, assetByte, 32, bytes.length);
            }
            Double fromBalance = asset1.getFromBalance();
            if (fromBalance != null) {
                byte[] bytes = ByteUtil.getBytes(fromBalance);
                System.arraycopy(bytes, 0, assetByte, 48, bytes.length);
            }
            Double fromFronze = asset1.getFromFronze();
            if (fromFronze != null) {
                byte[] bytes = ByteUtil.getBytes(fromFronze);
                System.arraycopy(bytes, 0, assetByte, 56, bytes.length);
            }
            Double toBalance = asset1.getToBalance();
            if (toBalance != null) {
                byte[] bytes = ByteUtil.getBytes(toBalance);
                System.arraycopy(bytes, 0, assetByte, 64, bytes.length);
            }
            Double toFronze = asset1.getToFronze();
            if (toFronze != null) {
                byte[] bytes = ByteUtil.getBytes(toFronze);
                System.arraycopy(bytes, 0, assetByte, 72, bytes.length);
            }
            Long createTime = asset1.getCreateTime();
            if (createTime != null) {
                byte[] bytes = ByteUtil.getBytes(createTime);
                System.arraycopy(bytes, 0, assetByte, 80, bytes.length);
            }
            StockHolder stockHolder = asset1.getStockHolder();
            String userId = stockHolder.getUserId();
            if (userId != null) {
                byte[] bytes = ByteUtil.getBytes(userId);
                System.arraycopy(bytes, 0, assetByte, 88, bytes.length);
            }
            System.arraycopy(assetByte, 0, assetBytes, (i - 1) * 100, 100);
        }
        MessageProtocol result = new MessageProtocol();
        byte[] content = new byte[36 + 100 * assets.size()];
        String comment = ResponseMsg.OK.getMsg();
        byte[] commentBytes = comment.getBytes();
        System.arraycopy(commentBytes, 0, content, 0, commentBytes.length);
        byte[] countBytes = ByteUtil.getBytes(count);
        System.arraycopy(countBytes, 0, content, 32, countBytes.length);
        System.arraycopy(assetBytes, 0, content, 36, assetBytes.length);
        result.setLen(68 + 100 * assets.size());
        result.setUiRetCode(ResponseMsg.OK.getRetCode());
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
