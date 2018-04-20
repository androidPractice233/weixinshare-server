package com.scut.weixinserver.model;

public enum ResultCode {
    SUCCESS(200,"操作成功"),

    SERVER_ERROR(500, "服务器内部错误"),
    //用户相关
    USER_NOT_EXIST(501,"用户不存在"),
    USER_PASS_ERR(502,"密码错误"),
    USER_NAME_ERR(503,"用户名错误"),
    USER_ID_EXIST(504,"该用户已经存在"),
    USER_OLD_PASS_ERR(505,"原密码错误"),

    //动态相关
    MOMENT_NOT_UPDATE(506, "无更新动态"),
    MOMENT_NOT_EXIST(507, "动态不存在"),
    COMMENT_NOT_UPDATE(508, "无更新评论"),
    COMMENT_NOT_EXIST(509, "评论不存在"),

    //上传图片
    IMG_NOT_ALLOW(510, "图片错误");




    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return this.code;
    }

    public String msg() {
        return this.msg;
    }
}
