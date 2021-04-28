package com.huayun.bond.service;

import com.huayun.bond.dao.OrderDao;
import com.huayun.bond.pojo.MessageProtocol;
import com.huayun.bond.pojo.Order;
import com.huayun.bond.pojo.ResponseMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
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
public class OrderService1 {
    @Autowired
    private OrderDao orderDao;

    /*订单查询*/
    public MessageProtocol qryOrder(MessageProtocol msg) throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.copiedBuffer(msg.getContent());
        String clOrdID = byteBuf.readCharSequence(10, CharsetUtil.UTF_8).toString();
        String securityId = byteBuf.readCharSequence(8, CharsetUtil.UTF_8).toString();
        int page = byteBuf.readInt();
        int num = byteBuf.readInt();
        long startTime = byteBuf.readLong();
        long endTime = byteBuf.readLong();
        List<Order> orders = orderDao.qryOrder(clOrdID, securityId, (page - 1) * num, num, startTime, endTime);
        int count = orderDao.qryOrderCount(clOrdID, securityId, startTime, endTime);
        ByteBuf buffer = Unpooled.buffer(107 * orders.size());
        //数据转化为byte[]
        for (Order order : orders) {
            buffer.writeBytes(Unpooled.copiedBuffer(order.getSubmittingPBUID(),0,6,CharsetUtil.UTF_8),6);
            buffer.writeBytes(Unpooled.copiedBuffer(order.getSecurityID(),0,8, CharsetUtil.UTF_8),8);
            buffer.writeBytes(Unpooled.copiedBuffer(order.getSecurityIDSource(),0,4,CharsetUtil.UTF_8),4);
            buffer.writeShort(order.getOwnerType());
            buffer.writeBytes(Unpooled.copiedBuffer(order.getClearingFirm(),0,2,CharsetUtil.UTF_8),2);
            buffer.writeLong(order.getTransactTime());
            buffer.writeBytes(Unpooled.copiedBuffer(order.getUserInfo(),0,8,CharsetUtil.UTF_8),8);
            buffer.writeBytes(Unpooled.copiedBuffer(order.getClOrdID(),0,10,CharsetUtil.UTF_8),10);
            buffer.writeBytes(Unpooled.copiedBuffer(order.getAccountID(),0,12, CharsetUtil.UTF_8),12);
            buffer.writeBytes(Unpooled.copiedBuffer(order.getBranchID(),0,4,CharsetUtil.UTF_8),4);
            buffer.writeBytes(Unpooled.copiedBuffer(order.getOrderRestrictions(),0,4,CharsetUtil.UTF_8),4);
            buffer.writeByte(order.getSide());
            buffer.writeByte(order.getOrdType());
            buffer.writeLong(order.getOrderQty());
            buffer.writeLong(order.getPrice());
            buffer.writeLong(order.getStopPx());
            buffer.writeLong(order.getMinQty());
            buffer.writeShort(order.getMaxPriceLevels());
            buffer.writeByte(order.getTimeInforce());
            buffer.writeByte(order.getCashMargin());
            buffer.writeByte(order.getOrdStatus());
        }
        MessageProtocol result = new MessageProtocol();
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
        byte[] content=new byte[orders.size()*107];
        buffer.readBytes(content);
        result.setContent(content);
        return result;
    }
}
