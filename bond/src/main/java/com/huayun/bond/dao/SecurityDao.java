package com.huayun.bond.dao;

import com.huayun.bond.pojo.Security;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SecurityDao {
    List<Security> qrySecurity(@Param("securityID") String securityId, @Param("offset") int offset, @Param("row") int row);

    int qrySecurityCount(@Param("securityID") String securityId);
}
