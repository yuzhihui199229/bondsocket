package com.huayun.bond.service;

import com.huayun.bond.dao.SecurityDao;
import com.huayun.bond.pojo.MessageProtocol;
import com.huayun.bond.pojo.ResponseMsg;
import com.huayun.bond.pojo.Security;
import com.huayun.bond.util.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 股票，证券
 */
@Service
public class SecurityService {
    @Autowired
    private SecurityDao securityDao;

    public MessageProtocol qrySecurity(MessageProtocol msg) throws UnsupportedEncodingException {
        byte[] buf = msg.getContent();
        //转化为securityId
        byte[] securityIdBytes = new byte[9];
        System.arraycopy(buf, 32, securityIdBytes, 0, 8);
        String securityId = ByteUtil.getString(securityIdBytes);
        //转化为page
        byte[] pageBytes = new byte[4];
        System.arraycopy(buf, 40, pageBytes, 0, 4);
        int page = ByteUtil.getInt(pageBytes);
        //转化为num
        byte[] numBytes = new byte[4];
        System.arraycopy(buf, 44, pageBytes, 0, 4);
        int num = ByteUtil.getInt(pageBytes);
        int offset = (page - 1) * num;
        List<Security> securities = securityDao.qrySecurity(securityId, offset, num);
        int count = securityDao.qrySecurityCount(securityId);
        byte[] securityBytes = new byte[102 * securities.size()];
        int i = 0;
        for (Security security : securities) {
            ++i;
            byte[] securityByte = new byte[102];
            String securityID = security.getSecurityID();
            if (securityID!=null) {
                byte[] bytes = securityID.getBytes("UTF-8");
                System.arraycopy(bytes, 0, securityByte, 0, bytes.length);
            }
            String securityIDSource = security.getSecurityIDSource();
            if (securityIDSource!=null) {
                byte[] bytes = securityIDSource.getBytes("UTF-8");
                System.arraycopy(bytes,0,securityByte,8,bytes.length);
            }
            String securityName = security.getSecurityName();
            if (securityName != null) {
                byte[] bytes = securityName.getBytes("UTF-8");
                System.arraycopy(bytes,0,securityByte,12,bytes.length);
            }
            Short status = security.getStatus();
            if (status != null) {
                byte[] bytes = ByteUtil.getBytes(status);
                System.arraycopy(bytes,0,securityByte,44,bytes.length);
            }
            Integer minQty = security.getMinQty();
            if (minQty!=null) {
                byte[] bytes = ByteUtil.getBytes(minQty);
                System.arraycopy(bytes, 0, securityByte, 46, bytes.length);
            }
            Integer maxQty = security.getMaxQty();
            if (maxQty != null) {
                byte[] bytes = ByteUtil.getBytes(maxQty);
                System.arraycopy(bytes, 0, securityByte, 50, bytes.length);
            }
            Double feeRate = security.getFeeRate();
            if (feeRate != null) {
                byte[] bytes = ByteUtil.getBytes(feeRate);
                System.arraycopy(bytes, 0, securityByte, 54, bytes.length);
            }
            String tradeTimeGroup = security.getTradeTimeGroup();
            if (tradeTimeGroup != null) {
                byte[] bytes = tradeTimeGroup.getBytes("UTF-8");
                System.arraycopy(bytes, 0, securityByte, 62, bytes.length);
            }
            Long updateTime = security.getUpdateTime();
            if (updateTime!=null) {
                byte[] bytes = ByteUtil.getBytes(updateTime);
                System.arraycopy(bytes, 0, securityByte, 94, bytes.length);
            }
            System.arraycopy(securityByte,0,securityBytes,(i-1)*102,102);
        }
        MessageProtocol result = new MessageProtocol();
        byte[] content = new byte[36 + 102 * securities.size()];
        String comment = ResponseMsg.OK.getMsg();
        byte[] commentBytes = comment.getBytes();
        System.arraycopy(commentBytes, 0, content, 0, commentBytes.length);
        byte[] countBytes = ByteUtil.getBytes(count);
        System.arraycopy(countBytes, 0, content, 32, countBytes.length);
        System.arraycopy(securityBytes, 0, content, 36, securityBytes.length);
        result.setLen(68+102 * securities.size());
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
