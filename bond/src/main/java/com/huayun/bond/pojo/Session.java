package com.huayun.bond.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Session implements Serializable {
    private String userId;
    private String sessionId;
    private String token;
    private String clientType;
    private Long createTime;
    private Long updateTime;
    private int tokenTime;
}
