package com.scut.weixinserver.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Moment {
    @Id
    private String momentId;
    private String userId;
    private String textContent;
    private String picContent;
    private Date createTime;
    private double longitude; //经度
    private double latitude; //纬度
    private String location;//为了不重复用经纬度去获取地名
    private Date updateTime;//记录评论的最后更新时间


    public Moment() {
    }

    @Override
    public String toString() {
        return "Moment{" +
                "momentId='" + momentId + '\'' +
                ", userId='" + userId + '\'' +
                ", textContent='" + textContent + '\'' +
                ", picContent='" + picContent + '\'' +
                ", createTime=" + createTime +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", location='" + location + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }

    public Moment(String momentId, String userId, String textContent, String picContent, Date createTime, double longitude, double latitude, String location, Date updateTime) {
        this.momentId = momentId;
        this.userId = userId;
        this.textContent = textContent;
        this.picContent = picContent;
        this.createTime = createTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
        this.updateTime = updateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getPicContent() {
        return picContent;
    }

    public void setPicContent(String picContent) {
        this.picContent = picContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
