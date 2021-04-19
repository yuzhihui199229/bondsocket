package com.huayun.bond.pojo;

public enum ResponseMsg {
    OK(0,"成功"),
    USERLOSE(10000,"当前用户不存在"),
    USERISLOGIN(10001,"当前用户已登录"),
    TOKENLOSE(10002,"令牌不存在"),
    TOKENUNUSE(10003,"令牌无效"),
    LOGOUTOK(10004,"登出成功"),
    LOGOUTERROR(10004,"登出失败"),
    SYSERROR(20000,"系统错误");

    private Integer RetCode;
    private String msg;

    ResponseMsg(Integer retCode, String msg) {
        RetCode = retCode;
        this.msg = msg;
    }

    public Integer getRetCode() {
        return RetCode;
    }

    public void setRetCode(Integer retCode) {
        RetCode = retCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
