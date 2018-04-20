package com.scut.weixinserver.model;

public class CommentReq {
    private long dateTime;

    private int pageNum;

    private int pageSize;


    public CommentReq() {
    }

    @Override
    public String toString() {
        return "CommentReq{" +
                "dateTime=" + dateTime +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
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

    public CommentReq(long dateTime, int pageNum, int pageSize) {
        this.dateTime = dateTime;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
