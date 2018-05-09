package com.scut.weixinserver.model;

public class CommentReq {
    private long dateTime;

    private int pageNum;

    private int pageSize;

    private String userId;


    public CommentReq() {
    }

    @Override
    public String toString() {
        return "CommentReq{" +
                "dateTime=" + dateTime +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", userId='" + userId + '\'' +
                '}';
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public CommentReq(long dateTime, int pageNum, int pageSize, String userId) {
        this.dateTime = dateTime;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.userId = userId;
    }
}
