package com.huayun.bond.service;

import com.huayun.bond.dao.UserDao;
import com.huayun.bond.pojo.MessageProtocol;
import com.huayun.bond.pojo.ResponseMsg;
import com.huayun.bond.pojo.UserInfo;
import com.huayun.bond.util.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 用户
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public MessageProtocol qryUserInfo(MessageProtocol msg) throws UnsupportedEncodingException {
        byte[] buf = msg.getContent();
        //转化为userid
        byte[] userByte = new byte[13];
        System.arraycopy(buf, 32, userByte, 0, 12);
        String userId = ByteUtil.getString(userByte);
        //转化为status
        byte[] statusByte=new byte[3];
        System.arraycopy(buf, 44, statusByte, 0, 2);
        char statusChar = ByteUtil.getChar(statusByte);
        //转化为userPropty
        byte[] userproptyByte=new byte[3];
        System.arraycopy(buf, 46, userproptyByte, 0, 2);
        char userproptychar = ByteUtil.getChar(userproptyByte);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setStatus(statusChar);
        userInfo.setUserPropty(userproptychar);
        List<UserInfo> userInfos = userDao.qryUserInfo(userInfo);
        int count = userDao.qryUserInfoCount(userInfo);
        byte[] userInfoBytes = new byte[132 * userInfos.size()];
        int i = 0;
        for (UserInfo info : userInfos) {
            ++i;
            byte[] userInfoByte = new byte[132];
            String userId1 = info.getUserId();
            if (userId != null) {
                byte[] bytes = userId1.getBytes("UTF-8");
                System.arraycopy(bytes, 0, userInfoByte, 0, bytes.length);
            }
            String userName = info.getUserName();
            if (userName != null) {
                byte[] bytes = userName.getBytes("UTF-8");
                System.arraycopy(bytes, 0, userInfoByte, 12, bytes.length);
            }
            String companyId = info.getCompanyId();
            if (companyId != null) {
                byte[] bytes = companyId.getBytes("UTF-8");
                System.arraycopy(bytes, 0, userInfoByte, 44, bytes.length);
            }
            String userpassword = info.getUserpassword();
            if (userpassword != null) {
                byte[] bytes = userpassword.getBytes("UTF-8");
                System.arraycopy(bytes, 0, userInfoByte, 56, bytes.length);
            }
            String phone = info.getPhone();
            if (phone != null) {
                byte[] bytes = phone.getBytes("UTF-8");
                System.arraycopy(bytes, 0, userInfoByte, 88, bytes.length);
            }
            String userAccount = info.getUserAccount();
            if (userAccount != null) {
                byte[] bytes = userAccount.getBytes("UTF-8");
                System.arraycopy(bytes, 0, userInfoByte, 100, bytes.length);
            }
            Character userPropty1 = info.getUserPropty();
            if (userPropty1!=null) {
                byte[] bytes = ByteUtil.getBytes(userPropty1);
                System.arraycopy(bytes,0,userInfoByte,112,bytes.length);
            }
            Character status = info.getStatus();
            if (status != null) {
                byte[] bytes = ByteUtil.getBytes(status);
                System.arraycopy(bytes, 0, userInfoByte, 114, bytes.length);
            }
            Long createTime = info.getCreateTime();
            if (createTime != null) {
                byte[] bytes = ByteUtil.getBytes(createTime);
                System.arraycopy(bytes, 0, userInfoByte, 116, bytes.length);
            }
            Long updateTime = info.getUpdateTime();
            if (updateTime != null) {
                byte[] bytes = ByteUtil.getBytes(updateTime);
                System.arraycopy(bytes, 0, userInfoByte, 124, bytes.length);
            }
            System.arraycopy(userInfoByte, 0, userInfoBytes,(i - 1) * 132, 132);
        }
        MessageProtocol result = new MessageProtocol();
        byte[] content = new byte[36 + 132 * userInfos.size()];
        String comment = ResponseMsg.OK.getMsg();
        byte[] commentBytes = comment.getBytes();
        System.arraycopy(commentBytes, 0, content, 0, commentBytes.length);
        byte[] countBytes = ByteUtil.getBytes(count);
        System.arraycopy(countBytes, 0, content, 32, countBytes.length);
        System.arraycopy(userInfoBytes, 0, content, 36, userInfoBytes.length);
        result.setLen(68 + 132 * userInfos.size());
        result.setUiRetCode(ResponseMsg.OK.getRetCode());
        result.setSzMagicNum(msg.getSzMagicNum());
        result.setByVersion(msg.getByVersion());
        result.setByMsgType((byte)2);
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
