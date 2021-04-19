package com.huayun.bond.pojo;

import lombok.Data;

@Data
public class MessageProtocol {
    private int len;
    private short szMagicNum;
    private byte byVersion;
    private byte byMsgType;
    private short uiSourceID;
    private int uiSessionID;
    private short uiFuncNo;
    private int uiMsgSeq;
    private int uiRetCode;
    private byte uiMktCode;
    private byte[] byReserved;
    private byte[] content;
}
