package com.huayun.bond.service;

import com.huayun.bond.dao.RecallOrderDao;
import com.huayun.bond.pojo.MessageProtocol;
import com.huayun.bond.pojo.Order;
import com.huayun.bond.pojo.RecallOrder;
import com.huayun.bond.pojo.ResponseMsg;
import com.huayun.bond.util.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 撤单
 */
@Service
public class RecallOrderService {
    @Autowired
    private RecallOrderDao recallOrderDao;

    public MessageProtocol qryRecallOrder(MessageProtocol msg) throws UnsupportedEncodingException {
        byte[] buf = msg.getContent();
        //转化为clOrdID
        byte[] clOrdIDByte = new byte[11];
        System.arraycopy(buf, 32, clOrdIDByte, 0, 10);
        String clOrdID = ByteUtil.getString(clOrdIDByte);
        byte[] securityBytes = new byte[9];
        System.arraycopy(buf, 42, securityBytes, 0, 8);
        String securityId = ByteUtil.getString(securityBytes);
        //转化为page
        byte[] pageByte = new byte[4];
        System.arraycopy(buf, 50, pageByte, 0, 4);
        int page = ByteUtil.getInt(pageByte);
        //转化为num
        byte[] numByte = new byte[4];
        System.arraycopy(buf, 54, numByte, 0, 4);
        int num = ByteUtil.getInt(numByte);
        //startTime
        byte[] startTimeByte = new byte[8];
        System.arraycopy(buf, 58, startTimeByte, 0, 8);
        long startTime = ByteUtil.getLong(startTimeByte);
        //endTime
        byte[] endTimeByte = new byte[8];
        System.arraycopy(buf, 66, endTimeByte, 0, 8);
        long endTime = ByteUtil.getLong(endTimeByte);
        List<RecallOrder> recallOrders = recallOrderDao.qryRecallOrder(clOrdID,securityId, (page - 1) * num, num, startTime, endTime);
        int count = recallOrderDao.qryRecallOrderCount(clOrdID,securityId, startTime, endTime);
        byte[] recallOrderDataBytes = new byte[132 * recallOrders.size()];
        int i = 0;
        for (RecallOrder recallOrder : recallOrders) {
            ++i;
            byte[] recallOrderDataByte = new byte[132];
            String submittingPBUID = recallOrder.getSubmittingPBUID();
            if (submittingPBUID != null) {
                byte[] submittingbytes = submittingPBUID.getBytes("UTF-8");
                System.arraycopy(submittingbytes, 0, recallOrderDataByte, 0, submittingbytes.length);
            }
            String securityID = recallOrder.getSecurityID();
            if (securityID != null) {
                byte[] securityIdBytes = securityID.getBytes("UTF-8");
                System.arraycopy(securityIdBytes, 0, recallOrderDataByte, 6, securityIdBytes.length);
            }
            String securityIDSource = recallOrder.getSecurityIDSource();
            if (securityIDSource != null) {
                byte[] securityIdSourceBytes = securityIDSource.getBytes("UTF-8");
                System.arraycopy(securityIdSourceBytes, 0, recallOrderDataByte, 14, securityIdSourceBytes.length);
            }
            Short ownerType = recallOrder.getOwnerType();
            if (ownerType != null) {
                byte[] ownerTypeBytes = ByteUtil.getBytes(ownerType);
                System.arraycopy(ownerTypeBytes, 0, recallOrderDataByte, 18, ownerTypeBytes.length);
            }
            String clearingFirm = recallOrder.getClearingFirm();
            if (clearingFirm != null) {
                byte[] clearingFirmBytes = clearingFirm.getBytes("UTF-8");
                System.arraycopy(clearingFirmBytes, 0, recallOrderDataByte, 20, clearingFirmBytes.length);
            }
            Long transactTime = recallOrder.getTransactTime();
            if (transactTime != null) {
                byte[] transactTimeBytes = ByteUtil.getBytes(transactTime);
                System.arraycopy(transactTimeBytes, 0, recallOrderDataByte, 22, transactTimeBytes.length);
            }
            String userInfo = recallOrder.getUserInfo();
            if (userInfo != null) {
                byte[] userInfoBytes = userInfo.getBytes("UTF-8");
                System.arraycopy(userInfoBytes, 0, recallOrderDataByte, 30, userInfoBytes.length);
            }
            String clOrdID1 = recallOrder.getClOrdID();
            if (clOrdID1 != null) {
                byte[] clOrdbytes = clOrdID1.getBytes("UTF-8");
                System.arraycopy(clOrdbytes, 0, recallOrderDataByte, 38, clOrdbytes.length);
            }
            String origClOrdID = recallOrder.getOrigClOrdID();
            if (origClOrdID != null) {
                byte[] origClOrdIDBytes = origClOrdID.getBytes("UTF-8");
                System.arraycopy(origClOrdIDBytes, 0, recallOrderDataByte, 48, origClOrdIDBytes.length);
            }
            Byte side = recallOrder.getSide();
            if (side != null) {
                recallOrderDataByte[58] = side;
            }
            String orderID = recallOrder.getOrderID();
            if (orderID != null) {
                byte[] orderIDBytes = orderID.getBytes("UTF-8");
                System.arraycopy(orderIDBytes, 0, recallOrderDataByte, 59, orderIDBytes.length);
            }
            Long orderQty = recallOrder.getOrderQty();
            if (orderQty != null) {
                byte[] orderQtyBytes = ByteUtil.getBytes(orderQty);
                System.arraycopy(orderQtyBytes, 0, recallOrderDataByte, 75, orderQtyBytes.length);
            }
            Byte ordStatus = recallOrder.getOrdStatus();
            if (ordStatus != null) {
                recallOrderDataByte[83] = ordStatus;
            }
            Order order = recallOrder.getOrder();
            Byte ordType = order.getOrdType();
            if (ordType != null) {
                recallOrderDataByte[84] = ordType;
            }
            Long price = order.getPrice();
            if (price != null) {
                byte[] bytes = ByteUtil.getBytes(price);
                System.arraycopy(bytes, 0, recallOrderDataByte, 85, bytes.length);
            }
            String accountID = order.getAccountID();
            if (accountID != null) {
                byte[] bytes = ByteUtil.getBytes(accountID);
                System.arraycopy(bytes, 0, recallOrderDataByte, 93, bytes.length);
            }
            String branchID = order.getBranchID();
            if (branchID != null) {
                byte[] bytes = ByteUtil.getBytes(branchID);
                System.arraycopy(bytes, 0, recallOrderDataByte, 105, bytes.length);
            }
            String orderRestrictions = order.getOrderRestrictions();
            if (orderRestrictions != null) {
                byte[] bytes = ByteUtil.getBytes(orderRestrictions);
                System.arraycopy(bytes, 0, recallOrderDataByte, 109, bytes.length);
            }
            Long minQty = order.getMinQty();
            if (minQty != null) {
                byte[] bytes = ByteUtil.getBytes(minQty);
                System.arraycopy(bytes, 0, recallOrderDataByte, 113, bytes.length);
            }
            Short maxPriceLevels = order.getMaxPriceLevels();
            if (maxPriceLevels != null) {
                byte[] bytes = ByteUtil.getBytes(maxPriceLevels);
                System.arraycopy(bytes, 0, recallOrderDataByte, 121, bytes.length);
            }
            Byte timeInforce = order.getTimeInforce();
            if (timeInforce != null) {
                recallOrderDataByte[123] = timeInforce;
            }
            Long orderQty101 = order.getOrderQty();
            if (orderQty101!=null) {
                byte[] bytes = ByteUtil.getBytes(orderQty101);
                System.arraycopy(bytes,0,recallOrderDataByte,124,bytes.length);
            }
            System.arraycopy(recallOrderDataByte, 0, recallOrderDataBytes, (i - 1) * 132, 132);
        }
        MessageProtocol result = new MessageProtocol();
        byte[] content = new byte[36 + 132 * recallOrders.size()];
        String comment = ResponseMsg.OK.getMsg();
        byte[] commentBytes = comment.getBytes();
        System.arraycopy(commentBytes, 0, content, 0, commentBytes.length);
        byte[] countBytes = ByteUtil.getBytes(count);
        System.arraycopy(countBytes, 0, content, 32, countBytes.length);
        System.arraycopy(recallOrderDataBytes, 0, content, 36, recallOrderDataBytes.length);
        result.setLen(68 + 132 * recallOrders.size());
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
