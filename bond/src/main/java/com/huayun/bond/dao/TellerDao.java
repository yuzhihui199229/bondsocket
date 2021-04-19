package com.huayun.bond.dao;

import com.huayun.bond.pojo.Session;
import com.huayun.bond.pojo.TellerInfo;
import com.huayun.bond.pojo.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TellerDao {
    UserInfo login(UserInfo userInfo);

    int addSession(UserInfo userInfo, @Param("session") String session);

    Session getToken(Session session);

    int updateToken(@Param("token") String token);

    int updateStatus(UserInfo userInfo);

    List<TellerInfo> qryTellerInfo(TellerInfo tellerInfo);

    int qryTellerInfoCount(TellerInfo tellerInfo);
}
