package com.huayun.bond.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * result实体类
 */
@Data
public class Result {
    private Integer code;    //返回码
    private byte[] data;     //返回数据

    public Result() {
    }

    public Result(Integer code, byte[] data) {
        this.code = code;
        this.data = data;
    }
}
