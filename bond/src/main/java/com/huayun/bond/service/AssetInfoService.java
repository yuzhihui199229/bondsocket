package com.huayun.bond.service;

import com.huayun.bond.dao.AssetInfoDao;
import com.huayun.bond.pojo.AssetInfo;
import com.huayun.bond.pojo.MessageProtocol;
import com.huayun.bond.pojo.ResponseMsg;
import com.huayun.bond.util.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 资金信息
 */
@Service
public class AssetInfoService {
    @Autowired
    private AssetInfoDao assetInfoDao;

    public MessageProtocol qryAssetInfo(MessageProtocol msg) throws UnsupportedEncodingException {
        byte[] buf = msg.getContent();
        byte[] userIdBytes = new byte[13];
        System.arraycopy(buf, 32, userIdBytes, 0, 12);
        String userId = ByteUtil.getString(userIdBytes);
        List<AssetInfo> assetInfos = assetInfoDao.qryAssetInfo(userId);
        int count = assetInfoDao.qryAssetInfoCount(userId);
        byte[] assetInfoBytes = new byte[76*count];
        int i = 0;
        for (AssetInfo assetInfo : assetInfos) {
            ++i;
            byte[] assetInfoByte = new byte[76];
            String userId1 = assetInfo.getUserId();
            if (userId1 != null) {
                byte[] bytes = userId1.getBytes("UTF-8");
                System.arraycopy(bytes, 0, assetInfoByte, 0, bytes.length);
            }
            String assetAccount = assetInfo.getAssetAccount();
            if (assetAccount != null) {
                byte[] bytes = assetAccount.getBytes("UTF-8");
                System.arraycopy(bytes, 0, assetInfoByte, 12, bytes.length);
            }
            Double balance = assetInfo.getBalance();
            if (balance != null) {
                byte[] bytes = ByteUtil.getBytes(balance);
                System.arraycopy(bytes, 0, assetInfoByte, 44, bytes.length);
            }
            Double frozen = assetInfo.getFrozen();
            if (frozen != null) {
                byte[] bytes = ByteUtil.getBytes(frozen);
                System.arraycopy(bytes, 0, assetInfoByte, 52, bytes.length);
            }
            Long createTime = assetInfo.getCreateTime();
            if (createTime != null) {
                byte[] bytes = ByteUtil.getBytes(createTime);
                System.arraycopy(bytes, 0, assetInfoByte, 60, bytes.length);
            }
            Long updateTime = assetInfo.getUpdateTime();
            if (updateTime != null) {
                byte[] bytes = ByteUtil.getBytes(updateTime);
                System.arraycopy(bytes, 0, assetInfoByte, 68, bytes.length);
            }
            System.arraycopy(assetInfoByte, 0, assetInfoBytes, (i - 1) * 76, 76);
        }
        MessageProtocol result = new MessageProtocol();
        byte[] content = new byte[36 + 76 * count];
        String comment = ResponseMsg.OK.getMsg();
        byte[] commentBytes = comment.getBytes();
        System.arraycopy(commentBytes, 0, content, 0, commentBytes.length);
        byte[] countBytes = ByteUtil.getBytes(count);
        System.arraycopy(countBytes, 0, content, 32, countBytes.length);
        System.arraycopy(assetInfoBytes, 0, content, 36, assetInfoBytes.length);
        result.setLen(68 + 76 * count);
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
