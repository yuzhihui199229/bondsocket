package com.huayun.bond.service;

import com.huayun.bond.dao.OrderDao;
import com.huayun.bond.pojo.MessageProtocol;
import com.huayun.bond.pojo.Order;
import com.huayun.bond.pojo.ResponseMsg;
import com.huayun.bond.util.ByteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 订单
 */
@Service
@Slf4j
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    /*订单查询*/
    public MessageProtocol qryOrder(MessageProtocol msg) throws UnsupportedEncodingException {
        byte[] buf = msg.getContent();
        //转化为clOrdID
        byte[] clOrdIDByte = new byte[11];
        System.arraycopy(buf, 32, clOrdIDByte, 0, 10);
        String clOrdID = ByteUtil.getString(clOrdIDByte);
        //转化为page
        byte[] pageByte = new byte[4];
        System.arraycopy(buf, 42, pageByte, 0, 4);
        int page = ByteUtil.getInt(pageByte);
        //转化为num
        byte[] numByte = new byte[4];
        System.arraycopy(buf, 46, numByte, 0, 4);
        int num = ByteUtil.getInt(numByte);
        //startTime
        byte[] startTimeByte = new byte[8];
        System.arraycopy(buf, 50, startTimeByte, 0, 8);
        long startTime = ByteUtil.getLong(startTimeByte);
        //endTime
        byte[] endTimeByte = new byte[8];
        System.arraycopy(buf, 58, endTimeByte, 0, 8);
        long endTime = ByteUtil.getLong(endTimeByte);
        List<Order> orders = orderDao.qryOrder(clOrdID, (page - 1) * num, num,startTime,endTime);
        int count = orderDao.qryOrderCount(clOrdID,startTime,endTime);
        byte[] orderDataBytes = new byte[107 *orders.size()];
        int i = 0;
        //数据转化为byte[]
        for (Order order : orders) {
            ++i;
            byte[] orderDataByte = new byte[107];
            String submittingPBUID = order.getSubmittingPBUID();
            if (submittingPBUID != null) {
                byte[] submittingbytes = submittingPBUID.getBytes("UTF-8");
                System.arraycopy(submittingbytes, 0, orderDataByte, 0, submittingbytes.length);
            }
            String securityID = order.getSecurityID();
            if (securityID != null) {
                byte[] securityIdBytes = securityID.getBytes("UTF-8");
                System.arraycopy(securityIdBytes, 0, orderDataByte, 6, securityIdBytes.length);
            }
            String securityIDSource = order.getSecurityIDSource();
            if (securityIDSource != null) {
                byte[] securityIdSourceBytes = securityIDSource.getBytes("UTF-8");
                System.arraycopy(securityIdSourceBytes, 0, orderDataByte, 14, securityIdSourceBytes.length);
            }
            Short ownerType = order.getOwnerType();
            if (ownerType != null) {
                byte[] ownerTypeBytes = ByteUtil.getBytes(ownerType);
                System.arraycopy(ownerTypeBytes, 0, orderDataByte, 18, ownerTypeBytes.length);
            }
            String clearingFirm = order.getClearingFirm();
            if (clearingFirm != null) {
                byte[] clearingFirmBytes = clearingFirm.getBytes("UTF-8");
                System.arraycopy(clearingFirmBytes, 0, orderDataByte, 20, clearingFirmBytes.length);
            }
            Long transactTime = order.getTransactTime();
            if (transactTime != null) {
                byte[] transactTimeBytes = ByteUtil.getBytes(transactTime);
                System.arraycopy(transactTimeBytes, 0, orderDataByte, 22, transactTimeBytes.length);
            }
            String userInfo = order.getUserInfo();
            if (userInfo != null) {
                byte[] userInfoBytes = userInfo.getBytes("UTF-8");
                System.arraycopy(userInfoBytes, 0, orderDataByte, 30, userInfoBytes.length);
            }
            String clOrdID1 = order.getClOrdID();
            if (clOrdID1 != null) {
                byte[] clOrdbytes = clOrdID1.getBytes("UTF-8");
                System.arraycopy(clOrdbytes, 0, orderDataByte, 38, clOrdbytes.length);
            }
            String accountID = order.getAccountID();
            if (accountID != null) {
                byte[] accountIdBytes = accountID.getBytes("UTF-8");
                System.arraycopy(accountIdBytes, 0, orderDataByte, 48, accountIdBytes.length);
            }
            String branchID = order.getBranchID();
            if (branchID != null) {
                byte[] branchIdbytes = branchID.getBytes("UTF-8");
                System.arraycopy(branchIdbytes, 0, orderDataByte, 60, branchIdbytes.length);
            }
            String orderRestrictions = order.getOrderRestrictions();
            if (orderRestrictions != null) {
                byte[] orderRestrictionBytes = orderRestrictions.getBytes("UTF-8");
                System.arraycopy(orderRestrictionBytes, 0, orderDataByte, 64, orderRestrictionBytes.length);
            }
            Byte side = order.getSide();
            if (side != null) {
                orderDataByte[68] = side;
            }
            Byte ordType = order.getOrdType();
            if (ordType != null) {
                orderDataByte[69] = ordType;
            }
            Long orderQty = order.getOrderQty();
            if (orderQty != null) {
                byte[] orderQtyBytes = ByteUtil.getBytes(orderQty);
                System.arraycopy(orderQtyBytes, 0, orderDataByte, 70, orderQtyBytes.length);
            }
            Long price = order.getPrice();
            if (price != null) {
                byte[] priceBytes = ByteUtil.getBytes(price);
                System.arraycopy(priceBytes, 0, orderDataByte, 78, priceBytes.length);
            }
            Long stopPx = order.getStopPx();
            if (stopPx != null) {
                byte[] stopPxBytes = ByteUtil.getBytes(stopPx);
                System.arraycopy(stopPxBytes, 0, orderDataByte, 86, stopPxBytes.length);
            }
            Long minQty = order.getMinQty();
            if (minQty != null) {
                byte[] minQtyBytes = ByteUtil.getBytes(minQty);
                System.arraycopy(minQtyBytes, 0, orderDataByte, 94, minQtyBytes.length);
            }
            Short maxPriceLevels = order.getMaxPriceLevels();
            if (maxPriceLevels != null) {
                byte[] maxPriceLevelsBytes = ByteUtil.getBytes(maxPriceLevels);
                System.arraycopy(maxPriceLevelsBytes, 0, orderDataByte, 102, maxPriceLevelsBytes.length);
            }
            Byte timeInforce = order.getTimeInforce();
            if (timeInforce != null) {
                orderDataByte[104] = order.getTimeInforce();
            }
            Byte cashMargin = order.getCashMargin();
            if (cashMargin != null) {
                orderDataByte[105] = cashMargin;
            }
            Byte ordStatus = order.getOrdStatus();
            if (ordStatus != null) {
                orderDataByte[106] = ordStatus;
            }
            System.arraycopy(orderDataByte, 0, orderDataBytes, (i - 1) * 107, 107);
        }
        MessageProtocol result = new MessageProtocol();
        byte[] content = new byte[36 + 107 * orders.size()];
        String comment = ResponseMsg.OK.getMsg();
        byte[] commentBytes = comment.getBytes();
        System.arraycopy(commentBytes, 0, content, 0, commentBytes.length);
        byte[] countBytes = ByteUtil.getBytes(count);
        System.arraycopy(countBytes, 0, content, 32, countBytes.length);
        System.arraycopy(orderDataBytes, 0, content, 36, orderDataBytes.length);
        result.setLen(68 + 107 * orders.size());
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
