package com.scut.weixinserver.model;

import java.io.Serializable;

public class Result<T> implements Serializable{
    private int code;//返回码

    private String msg;

    private T data;

    public Result() {
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public void setCodeAndMsg(ResultCode rc) {
        this.code = rc.code();
        this.msg = rc.msg();
    }

    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
