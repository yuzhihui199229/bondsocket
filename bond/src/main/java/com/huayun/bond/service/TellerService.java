package com.huayun.bond.service;

import com.huayun.bond.dao.TellerDao;
import com.huayun.bond.pojo.*;
import com.huayun.bond.util.ByteStringTrans;
import com.huayun.bond.util.ByteUtil;
import com.huayun.bond.util.CodeDigest;
import com.huayun.bond.util.DataTypeChangeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 柜员
 */
@Slf4j
@Service
public class TellerService {
    @Autowired
    private TellerDao tellerDao;

    @Value("${netty.beatheart}")
    private short beatheart;

    public MessageProtocol login(MessageProtocol msg) {
        byte[] buf = msg.getContent();
        //转化为username
        byte[] userByte = new byte[13];
        System.arraycopy(buf, 0, userByte, 0, 12);
        String userId = ByteStringTrans.byteToStr(userByte);
        //转化为password
        byte[] passwordByte = new byte[33];
        System.arraycopy(buf, 12, passwordByte, 0, 32);
        String password = ByteStringTrans.byteToStr(passwordByte);
        //转化为type
        byte[] typeByte = new byte[2];
        System.arraycopy(buf, 44, typeByte, 0, 1);
        String type = ByteStringTrans.byteToStr(typeByte);
        //用户登录
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setUserpassword(password);
        UserInfo userInfoDb = tellerDao.login(userInfo);
        String comment = "";
        String session = "";
//        short beatheart = 30;
        MessageProtocol result = new MessageProtocol();
        if (userInfoDb == null) {
            result.setUiRetCode(ResponseMsg.USERLOSE.getRetCode());
            comment = ResponseMsg.USERLOSE.getMsg();
        }
        if (userInfoDb != null) {
//            if (userInfoDb.getStatus() == null || userInfoDb.getStatus() != 0) {//判断用户处于非登录状态
            result.setUiRetCode(ResponseMsg.OK.getRetCode());
            comment = ResponseMsg.OK.getMsg();
            SimpleDateFormat sm = new SimpleDateFormat("yyyyMMddhhmmss");
            String date = sm.format(new Date());
            session = CodeDigest.encryption(userId + password + date);
            /*修改登录状态*/
//                short status = 0;
//                userInfo.setStatus(status);
//                tellerDao.updateStatus(userInfo);
            /*创建会话令牌*/
            tellerDao.addSession(userInfo, session);
//            } else {
//                result.setUiRetCode(ResponseMsg.USERISLOGIN.getRetCode());
//                comment = ResponseMsg.USERISLOGIN.getMsg();
//            }
        }
        byte[] content = new byte[66];
        byte[] commentBytes = comment.getBytes();
        byte[] sessionBytes = session.getBytes();
        byte[] beartheartbytes = DataTypeChangeHelper.shortToByteArray(beatheart);
        System.arraycopy(commentBytes, 0, content, 0, commentBytes.length);
        System.arraycopy(sessionBytes, 0, content, 32, sessionBytes.length);
        System.arraycopy(beartheartbytes, 0, content, 64, beartheartbytes.length);
        result.setLen(32 + content.length);
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

    /*获取会话令牌*/
    public MessageProtocol getToken(MessageProtocol msg) {
        byte[] buf = msg.getContent();
//        //userid
//        byte[] userIdByte = new byte[13];
//        System.arraycopy(buf, 0, userIdByte, 0, 12);
//        String userId = ByteStringTrans.byteToStr(userIdByte);
        byte[] tokenByte = new byte[33];
        System.arraycopy(buf, 0, tokenByte, 0, 32);
        String token = ByteStringTrans.byteToStr(tokenByte);
        Session session = new Session();
        session.setToken(token);
        Session sessionDb = tellerDao.getToken(session);
        MessageProtocol result = new MessageProtocol();
        String comment = "";
        if (sessionDb == null) {
            comment = ResponseMsg.TOKENLOSE.getMsg();
            result.setUiRetCode(ResponseMsg.TOKENLOSE.getRetCode());
        }
        if (sessionDb != null) {
            if (sessionDb.getTokenTime() <= beatheart*20) {
                comment = ResponseMsg.OK.getMsg();
                result.setUiRetCode(ResponseMsg.OK.getRetCode());
                //更新令牌时间
                tellerDao.updateToken(token);
            } else {
//                UserInfo userInfo = new UserInfo();
//                userInfo.setUserId(sessionDb.getUserId());
//                short status = 1;
//                userInfo.setStatus(status);
//                int i = tellerDao.updateStatus(userInfo);
                comment = ResponseMsg.TOKENUNUSE.getMsg();
                result.setUiRetCode(ResponseMsg.TOKENUNUSE.getRetCode());
            }
        }
        byte[] content = new byte[32];
        byte[] commentBytes = comment.getBytes();
        System.arraycopy(commentBytes, 0, content, 0, commentBytes.length);
        result.setLen(32 + content.length);
        result.setSzMagicNum(msg.getSzMagicNum());
        result.setByVersion(msg.getByVersion());
        byte i = 2;
        result.setByMsgType(i);
        result.setUiSourceID(msg.getUiSourceID());
        result.setUiSessionID(msg.getUiSessionID());
        result.setUiFuncNo(msg.getUiFuncNo());
        result.setUiMsgSeq(msg.getUiMsgSeq());
        result.setUiMktCode(msg.getUiMktCode());
        result.setByReserved(msg.getByReserved());
        result.setContent(content);
        return result;
    }

    public MessageProtocol logout(MessageProtocol msg) {
        byte[] buf = msg.getContent();
        //userid
        byte[] userIdByte = new byte[13];
        System.arraycopy(buf, 0, userIdByte, 0, 12);
        String userId = ByteStringTrans.byteToStr(userIdByte);
        /*修改登录状态*/
//        UserInfo userInfo = new UserInfo();
//        userInfo.setUserId(userId);
//        short status = 1;
//        userInfo.setStatus(status);
//        int i = tellerDao.updateStatus(userInfo);
        String comment = "登出成功";
        MessageProtocol result = new MessageProtocol();
//        if (i != 0) {
//            comment = ResponseMsg.LOGOUTOK.getMsg();
//            result.setUiRetCode(ResponseMsg.LOGOUTOK.getRetCode());
//        } else {
//            result.setUiRetCode(ResponseMsg.LOGOUTERROR.getRetCode());
//            comment = ResponseMsg.LOGOUTERROR.getMsg();
//        }
        comment = ResponseMsg.LOGOUTOK.getMsg();
        result.setUiRetCode(ResponseMsg.LOGOUTOK.getRetCode());
        byte b = 2;
        result.setByMsgType(b);
        byte[] content = new byte[32];
        byte[] commentBytes = comment.getBytes();
        System.arraycopy(commentBytes, 0, content, 0, commentBytes.length);
        result.setLen(32 + content.length);
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

    public MessageProtocol qryTellerInfo(MessageProtocol msg) throws UnsupportedEncodingException {
        byte[] buf = msg.getContent();
        //转化为username
        byte[] userByte = new byte[13];
        System.arraycopy(buf, 32, userByte, 0, 12);
        String userId = ByteUtil.getString(userByte);
        //转化为status
//        byte status = buf[44];
        //转化为userPropty
//        byte[] userProptyByte = new byte[3];
//        System.arraycopy(buf, 14, userProptyByte, 0, 2);
//        Short userPropty = ByteUtil.getShort(statusByte);
        TellerInfo tellerInfo = new TellerInfo();
        tellerInfo.setUserId(userId);
//        tellerInfo.setStatus(status);
        List<TellerInfo> tellerInfos = tellerDao.qryTellerInfo(tellerInfo);
        int count = tellerDao.qryTellerInfoCount(tellerInfo);
        int i = 0;
        byte[] tellerInfoBytes = new byte[158 * tellerInfos.size()];
        for (TellerInfo info : tellerInfos) {
            ++i;
            byte[] infoBytes = new byte[158];
            String userId1 = info.getUserId();
            if (userId1 != null) {
                byte[] bytes = userId1.getBytes("UTF-8");
                System.arraycopy(bytes, 0, infoBytes, 0, bytes.length);
            }
            String userName = info.getUserName();
            if (userName != null) {
                byte[] bytes = userName.getBytes("UTF-8");
                System.arraycopy(bytes, 0, infoBytes, 12, bytes.length);
            }
            String userpassword = info.getUserpassword();
            if (userpassword != null) {
                byte[] bytes = userpassword.getBytes("UTF-8");
                System.arraycopy(bytes, 0, infoBytes, 44, bytes.length);
            }
            Byte clientType = info.getClientType();
            if (clientType != null) {
                infoBytes[76] = clientType;
            }
            Byte status1 = info.getStatus();
            if (status1 != null) {
                infoBytes[77] = status1;
            }
            String roleId = info.getRoleId();
            if (roleId != null) {
                byte[] bytes = roleId.getBytes("UTF-8");
                System.arraycopy(bytes, 0, infoBytes, 78, bytes.length);
            }
            Long createTime = info.getCreateTime();
            if (createTime != null) {
                byte[] bytes = ByteUtil.getBytes(createTime);
                System.arraycopy(bytes, 0, infoBytes, 142, bytes.length);
            }
            Long updateTime = info.getUpdateTime();
            if (updateTime != null) {
                byte[] bytes = ByteUtil.getBytes(updateTime);
                System.arraycopy(bytes, 0, infoBytes, 150, bytes.length);
            }
            System.arraycopy(infoBytes, 0, tellerInfoBytes, (i - 1) * 158, 158);
        }
        MessageProtocol result = new MessageProtocol();
        byte[] content = new byte[36 + 158 * tellerInfos.size()];
        String comment = ResponseMsg.OK.getMsg();
        byte[] commentBytes = comment.getBytes();
        System.arraycopy(commentBytes, 0, content, 0, commentBytes.length);
        byte[] countBytes = ByteUtil.getBytes(count);
        System.arraycopy(countBytes, 0, content, 32, countBytes.length);
        System.arraycopy(tellerInfoBytes, 0, content, 36, tellerInfoBytes.length);
        result.setLen(68 + 158 * tellerInfos.size());
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
