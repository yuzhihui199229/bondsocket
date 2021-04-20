package com.huayun.bond.service;

import com.huayun.bond.dao.TradeTimeGroupDao;
import com.huayun.bond.pojo.MessageProtocol;
import com.huayun.bond.pojo.ResponseMsg;
import com.huayun.bond.pojo.TradeTimeInfo;
import com.huayun.bond.util.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交易时间组
 */
@Service
public class TradeTimeGroupService {
    @Autowired
    private TradeTimeGroupDao tradeTimeGroupDao;

    public MessageProtocol QryTradeTimeInfo(MessageProtocol msg) {
        byte[] buf = msg.getContent();
        byte[] tradeTimeGroupBytes = new byte[32];
        System.arraycopy(buf,0,tradeTimeGroupBytes,0,32);
        String tradeTimeGroup = ByteUtil.getString(tradeTimeGroupBytes);
        List<TradeTimeInfo> tradeTimeInfos = tradeTimeGroupDao.qryTradeTimeGroup(tradeTimeGroup);
        int count = tradeTimeGroupDao.qryCount(tradeTimeGroup);
        int i=0;
        byte[] tradeTimeInfoBytes = new byte[60*tradeTimeInfos.size()];
        for (TradeTimeInfo tradeTimeInfo : tradeTimeInfos) {
            i++;
            byte[] tradeTimeInfoByte = new byte[60];
            Integer id = tradeTimeInfo.getId();
            if (id != null) {
                byte[] bytes = ByteUtil.getBytes(id);
                System.arraycopy(bytes,0,tradeTimeInfoByte,0,bytes.length);
            }
            String tradeTimeGroup1 = tradeTimeInfo.getTradeTimeGroup();
            if (tradeTimeGroup1!=null) {
                byte[] bytes = ByteUtil.getBytes(tradeTimeGroup1);
                System.arraycopy(bytes,0,tradeTimeInfoByte,4,bytes.length);
            }
            Integer startTime = tradeTimeInfo.getStartTime();
            if (startTime!=null) {
                byte[] bytes = ByteUtil.getBytes(startTime);
                System.arraycopy(bytes,0,tradeTimeInfoByte,36,bytes.length);
            }
            Integer stopTime = tradeTimeInfo.getStopTime();
            if (stopTime != null) {
                byte[] bytes = ByteUtil.getBytes(stopTime);
                System.arraycopy(bytes,0,tradeTimeInfoByte,40,bytes.length);
            }
            Long createTime = tradeTimeInfo.getCreateTime();
            if (createTime != null) {
                byte[] bytes = ByteUtil.getBytes(createTime);
                System.arraycopy(bytes,0,tradeTimeInfoByte,44,bytes.length);
            }
            Long updateTime = tradeTimeInfo.getUpdateTime();
            if (updateTime!=null) {
                byte[] bytes = ByteUtil.getBytes(updateTime);
                System.arraycopy(bytes,0,tradeTimeInfoByte,52,bytes.length);
            }
            System.arraycopy(tradeTimeInfoByte,0,tradeTimeInfoBytes,(i-1)*60,60);
        }
        MessageProtocol result = new MessageProtocol();
        byte[] content = new byte[36 + 60 * tradeTimeInfos.size()];
        String comment = ResponseMsg.OK.getMsg();
        byte[] commentBytes = comment.getBytes();
        System.arraycopy(commentBytes, 0, content, 0, commentBytes.length);
        byte[] countBytes = ByteUtil.getBytes(count);
        System.arraycopy(countBytes, 0, content, 32, countBytes.length);
        System.arraycopy(tradeTimeInfoBytes, 0, content, 36, tradeTimeInfoBytes.length);
        result.setLen(68 + 60 * tradeTimeInfos.size());
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
