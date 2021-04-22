package com.huayun.bond.service;

import com.huayun.bond.dao.DealDao;
import com.huayun.bond.pojo.Deal;
import com.huayun.bond.pojo.MessageProtocol;
import com.huayun.bond.pojo.RecallOrderSuccess;
import com.huayun.bond.pojo.ResponseMsg;
import com.huayun.bond.util.ByteStringTrans;
import com.huayun.bond.util.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 成交
 */
@Service
public class DealService {
    @Autowired
    private DealDao dealDao;

    public MessageProtocol qryDeal(MessageProtocol msg) throws UnsupportedEncodingException {
        byte[] buf = msg.getContent();
        //转化为clOrdID
        byte[] clOrdIDByte = new byte[11];
        System.arraycopy(buf, 32, clOrdIDByte, 0, 10);
        String clOrdID = ByteUtil.getString(clOrdIDByte);
        //成交类型
        byte[] execIDBytes = new byte[17];
        System.arraycopy(buf,42,execIDBytes,0,16);
        String execID1 = ByteUtil.getString(execIDBytes);
        //转化为page
        byte[] pageByte = new byte[4];
        System.arraycopy(buf, 58, pageByte, 0, 4);
        int page = ByteUtil.getInt(pageByte);
        //转化为num
        byte[] numByte = new byte[4];
        System.arraycopy(buf, 62, numByte, 0, 4);
        int num = ByteUtil.getInt(numByte);
        byte[] startTimeByte = new byte[8];
        System.arraycopy(buf, 66, startTimeByte, 0, 8);
        long startTime = ByteUtil.getLong(startTimeByte);
        byte[] endTimeByte = new byte[8];
        System.arraycopy(buf, 74, endTimeByte, 0, 8);
        long endTime = ByteUtil.getLong(endTimeByte);
        List<Deal> deals = dealDao.qryDeal(clOrdID, execID1,(page - 1) * num, num, startTime, endTime);
        int count = dealDao.qryDealCount(clOrdID,execID1, startTime, endTime);
        byte[] dealBytes = new byte[211 * deals.size()];
        int i = 0;
        //数据转化
        for (Deal deal : deals) {
            ++i;
            byte[] dealByte = new byte[211];
            String accountID = deal.getAccountID();
            if (accountID != null) {
                byte[] accountIDBytes = accountID.getBytes("UTF-8");
                System.arraycopy(accountIDBytes, 0, dealByte, 0, accountIDBytes.length);
            }
            Long reportIndex = deal.getReportIndex();
            if (reportIndex != null) {
                byte[] bytes = ByteUtil.getBytes(reportIndex);
                System.arraycopy(bytes, 0, dealByte, 4, bytes.length);
            }
            String applid = deal.getApplID();
            if (applid != null) {
                byte[] bytes = applid.getBytes("UTF-8");
                System.arraycopy(bytes, 0, dealByte, 12, bytes.length);
            }
            String reportingPBUID = deal.getReportingPBUID();
            if (reportingPBUID != null) {
                byte[] bytes = reportingPBUID.getBytes("UTF-8");
                System.arraycopy(bytes, 0, dealByte, 15, bytes.length);
            }
            String submittingPBUID = deal.getSubmittingPBUID();
            if (submittingPBUID != null) {
                byte[] bytes = submittingPBUID.getBytes("UTF-8");
                System.arraycopy(bytes, 0, dealByte, 21, bytes.length);
            }
            String securityID = deal.getSecurityID();
            if (securityID != null) {
                byte[] bytes = securityID.getBytes("UTF-8");
                System.arraycopy(bytes, 0, dealByte, 27, bytes.length);
            }
            String securityIDSource = deal.getSecurityIDSource();
            if (securityIDSource != null) {
                byte[] bytes = securityIDSource.getBytes("UTF-8");
                System.arraycopy(bytes, 0, dealByte, 35, bytes.length);
            }
            Short ownerType = deal.getOwnerType();
            if (ownerType != null) {
                byte[] bytes = ByteUtil.getBytes(ownerType);
                System.arraycopy(bytes, 0, dealByte, 39, bytes.length);
            }
            String clearingFirm = deal.getClearingFirm();
            if (clearingFirm != null) {
                byte[] bytes = clearingFirm.getBytes("UTF-8");
                System.arraycopy(bytes, 0, dealByte, 41, bytes.length);
            }
            Long transactTime = deal.getTransactTime();
            if (transactTime != null) {
                byte[] bytes = ByteUtil.getBytes(transactTime);
                System.arraycopy(bytes, 0, dealByte, 43, bytes.length);
            }
            String userInfo = deal.getUserInfo();
            if (userInfo != null) {
                byte[] bytes = userInfo.getBytes("UTF-8");
                System.arraycopy(bytes, 0, dealByte, 51, bytes.length);
            }
            String orderID = deal.getOrderID();
            if (orderID != null) {
                byte[] bytes = orderID.getBytes("UTF-8");
                System.arraycopy(bytes, 0, dealByte, 59, bytes.length);
            }
            String clOrdID1 = deal.getClOrdID();
            if (clOrdID1 != null) {
                byte[] bytes = clOrdID1.getBytes("UTF-8");
                System.arraycopy(bytes, 0, dealByte, 75, bytes.length);
            }
            String execID = deal.getExecID();
            if (execID != null) {
                byte[] bytes = execID.getBytes("UTF-8");
                System.arraycopy(bytes, 0, dealByte, 85, bytes.length);
            }
            Byte execType = deal.getExecType();
            if (execType != null) {
                dealByte[101] = execType;
            }
            Byte ordStatus = deal.getOrdStatus();
            if (ordStatus != null) {
                dealByte[102] = ordStatus;
            }
            Long lastPx = deal.getLastPx();
            if (lastPx != null) {
                byte[] bytes = ByteUtil.getBytes(lastPx);
                System.arraycopy(bytes, 0, dealByte, 103, bytes.length);
            }
            Long lastQty = deal.getLastQty();
            if (lastQty != null) {
                byte[] bytes = ByteUtil.getBytes(lastQty);
                System.arraycopy(bytes, 0, dealByte, 111, bytes.length);
            }
            Long leavesQty = deal.getLeavesQty();
            if (leavesQty != null) {
                byte[] bytes = ByteUtil.getBytes(leavesQty);
                System.arraycopy(bytes, 0, dealByte, 119, bytes.length);
            }
            Long cumQty = deal.getCumQty();
            if (cumQty != null) {
                byte[] bytes = ByteUtil.getBytes(cumQty);
                System.arraycopy(bytes, 0, dealByte, 127, bytes.length);
            }
            Byte side = deal.getSide();
            if (side != null) {
                dealByte[135] = side;
            }
            String accountID1 = deal.getAccountID();
            if (accountID1 != null) {
                byte[] bytes = accountID1.getBytes("UTF-8");
                System.arraycopy(bytes, 0, dealByte, 136, bytes.length);
            }
            String branchID = deal.getBranchID();
            if (branchID != null) {
                byte[] bytes = branchID.getBytes("UTF-8");
                System.arraycopy(bytes, 0, dealByte, 148, bytes.length);
            }
            Byte cashMargin = deal.getCashMargin();
            if (cashMargin != null) {
                dealByte[152] = cashMargin;
            }
            RecallOrderSuccess recallOrderSuccess = deal.getRecallOrderSuccess();
            Byte ordType = recallOrderSuccess.getOrdType();
            if (ordType != null) {
                dealByte[153] = ordType;
            }
            Long orderQty = recallOrderSuccess.getOrderQty();
            if (orderQty != null) {
                byte[] bytes = ByteUtil.getBytes(orderQty);
                System.arraycopy(bytes, 0, dealByte, 154, bytes.length);
            }
            Long price = recallOrderSuccess.getPrice();
            if (price != null) {
                byte[] bytes = ByteUtil.getBytes(price);
                System.arraycopy(bytes, 0, dealByte, 162, bytes.length);
            }
            String orderRestrictions = recallOrderSuccess.getOrderRestrictions();
            if (orderRestrictions != null) {
                byte[] bytes = ByteUtil.getBytes(orderRestrictions);
                System.arraycopy(bytes, 0, dealByte, 170, bytes.length);
            }
            Long minQty = recallOrderSuccess.getMinQty();
            if (minQty != null) {
                byte[] bytes = ByteUtil.getBytes(minQty);
                System.arraycopy(bytes, 0, dealByte, 174, bytes.length);
            }
            Short maxPriceLevels = recallOrderSuccess.getMaxPriceLevels();
            if (maxPriceLevels != null) {
                byte[] bytes = ByteUtil.getBytes(maxPriceLevels);
                System.arraycopy(bytes, 0, dealByte, 182, bytes.length);
            }
            Byte timeInforce = recallOrderSuccess.getTimeInforce();
            if (timeInforce != null) {
                dealByte[184] = timeInforce;
            }
            String origClOrdId = recallOrderSuccess.getOrigClOrdId();
            if (origClOrdId != null) {
                byte[] bytes = ByteUtil.getBytes(origClOrdId);
                System.arraycopy(bytes, 0, dealByte, 185, bytes.length);
            }
            String origOrderId = recallOrderSuccess.getOrderId();
            if (origOrderId!=null) {
                byte[] bytes = ByteUtil.getBytes(origOrderId);
                System.arraycopy(bytes,0,dealByte,195,bytes.length);
            }
            System.arraycopy(dealByte, 0, dealBytes, (i - 1) * 211, dealByte.length);
        }
        //封装数据格式
        MessageProtocol result = new MessageProtocol();
        byte[] content = new byte[36 + 211 * deals.size()];
        String comment = ResponseMsg.OK.getMsg();
        byte[] commentBytes = comment.getBytes();
        System.arraycopy(commentBytes, 0, content, 0, commentBytes.length);
        byte[] countBytes = ByteUtil.getBytes(count);
        System.arraycopy(countBytes, 0, content, 32, countBytes.length);
        System.arraycopy(dealBytes, 0, content, 36, dealBytes.length);
        result.setLen(68 + 211 * deals.size());
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
