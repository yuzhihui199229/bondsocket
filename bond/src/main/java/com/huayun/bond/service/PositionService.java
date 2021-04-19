package com.huayun.bond.service;

import com.huayun.bond.dao.PositionDao;
import com.huayun.bond.pojo.MessageProtocol;
import com.huayun.bond.pojo.Position;
import com.huayun.bond.pojo.ResponseMsg;
import com.huayun.bond.util.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 持仓
 */
@Service
public class PositionService {
    @Autowired
    private PositionDao positionDao;

    public MessageProtocol qryPosition(MessageProtocol msg) throws UnsupportedEncodingException {
        byte[] buf = msg.getContent();
        //转化为accountID
        byte[] accountIDByte = new byte[13];
        System.arraycopy(buf, 32, accountIDByte, 0, 12);
        String accountID = ByteUtil.getString(accountIDByte);
        //转化为securityID
        byte[] securityIDByte = new byte[9];
        System.arraycopy(buf, 44, securityIDByte, 0, 8);
        String securityID = ByteUtil.getString(securityIDByte);
        //转化为page
        byte[] pageByte = new byte[4];
        System.arraycopy(buf, 52, pageByte, 0, 4);
        int page = ByteUtil.getInt(pageByte);
        //转化为num
        byte[] numByte = new byte[4];
        System.arraycopy(buf, 56, numByte, 0, 4);
        int num = ByteUtil.getInt(numByte);
        byte[] startTimeByte=new byte[8];
        System.arraycopy(buf, 60, startTimeByte, 0, 8);
        long startTime = ByteUtil.getLong(startTimeByte);
        byte[] endTimeByte = new byte[8];
        System.arraycopy(buf, 68, endTimeByte, 0, 8);
        long endTime = ByteUtil.getLong(endTimeByte);
        Position position = new Position();
        position.setAccountID(accountID);
        position.setSecurityID(securityID);
        //查询position
        List<Position> positions = positionDao.qryPosition(position, (page - 1) * num, num,startTime,endTime);
        int count = positionDao.qryPositionCount(position,startTime,endTime);
        int contentLen = count >= num ? num : count;
        byte[] positionBytes = new byte[100 * contentLen];
        int i = 0;
        for (Position positionBean : positions) {
            ++i;
            byte[] positionBeanByte = new byte[100];
            String positionBeanAccountID = positionBean.getAccountID();
            if (positionBeanAccountID != null) {
                byte[] accountIDBytes = positionBeanAccountID.getBytes("UTF-8");
                System.arraycopy(accountIDBytes, 0, positionBeanByte, 0, accountIDBytes.length);
            }
            String positionBeanSecurityID = positionBean.getSecurityID();
            if (positionBeanSecurityID != null) {
                byte[] securityIDBytes = positionBeanSecurityID.getBytes("UTF-8");
                System.arraycopy(securityIDBytes, 0, positionBeanByte, 12, securityIDBytes.length);
            }
            Long quantity = positionBean.getQuantity();
            if (quantity != null) {
                byte[] bytes = ByteUtil.getBytes(quantity);
                System.arraycopy(bytes,0,positionBeanByte,20,bytes.length);
            }
            Long originQty = positionBean.getOriginQty();
            if (originQty!=null) {
                byte[] bytes = ByteUtil.getBytes(originQty);
                System.arraycopy(bytes, 0, positionBeanByte, 28, bytes.length);
            }
            Double originOpenPrice = positionBean.getOriginOpenPrice();
            if (originOpenPrice != null) {
                byte[] bytes = ByteUtil.getBytes(originOpenPrice);
                System.arraycopy(bytes,0,positionBeanByte,36,bytes.length);
            }
            Long freeQty = positionBean.getFreeQty();
            if (freeQty != null) {
                byte[] bytes = ByteUtil.getBytes(freeQty);
                System.arraycopy(bytes,0,positionBeanByte,44,bytes.length);
            }
            Long frozenQty = positionBean.getFrozenQty();
            if (frozenQty!=null) {
                byte[] bytes = ByteUtil.getBytes(frozenQty);
                System.arraycopy(bytes,0,positionBeanByte,52,bytes.length);
            }
            Double price = positionBean.getPrice();
            if (price != null) {
                byte[] bytes = ByteUtil.getBytes(price);
                System.arraycopy(bytes,0,positionBeanByte,60,bytes.length);
            }
            Double profitAndLoss = positionBean.getProfitAndLoss();
            if (profitAndLoss!=null) {
                byte[] bytes = ByteUtil.getBytes(profitAndLoss);
                System.arraycopy(bytes,0,positionBeanByte,68,bytes.length);
            }
            Double rateOfReturn = positionBean.getRateOfReturn();
            if (rateOfReturn != null) {
                byte[] bytes = ByteUtil.getBytes(rateOfReturn);
                System.arraycopy(bytes,0,positionBeanByte,76,bytes.length);
            }
            Long createTime = positionBean.getCreateTime();
            if (createTime!=null) {
                byte[] bytes = ByteUtil.getBytes(createTime);
                System.arraycopy(bytes,0,positionBeanByte,84,bytes.length);
            }
            Long updateTime = positionBean.getUpdateTime();
            if (updateTime != null) {
                byte[] bytes = ByteUtil.getBytes(updateTime);
                System.arraycopy(bytes,0,positionBeanByte,92,bytes.length);
            }
            
            System.arraycopy(positionBeanByte, 0, positionBytes, (i - 1) * 100, 100);
        }
        MessageProtocol result = new MessageProtocol();
        byte[] content = new byte[36 + 100 * contentLen];
        String comment = ResponseMsg.OK.getMsg();
        byte[] commentBytes = comment.getBytes();
        System.arraycopy(commentBytes, 0, content, 0, commentBytes.length);
        byte[] countBytes = ByteUtil.getBytes(count);
        System.arraycopy(countBytes, 0, content, 32, countBytes.length);
        System.arraycopy(positionBytes, 0, content, 36, positionBytes.length);
        result.setLen(68 + 100 * count);
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
