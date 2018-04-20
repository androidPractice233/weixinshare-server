package com.scut.weixinserver.model;

public class MomentReq {

    private double latitude;

    private double longitude;

    private int pageNum;

    private int pageSize;

    private String ids;

    @Override
    public String toString() {
        return "MomentReq{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", ids='" + ids + '\'' +
                '}';
    }

    public MomentReq() {
    }

    public MomentReq(double latitude, double longitude, int pageNum, int pageSize, String ids) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.ids = ids;
    }



    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
}
