package com.scut.weixinserver.model;

import com.scut.weixinserver.entity.Moment;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

public class MomentBean {
    private String momentId;
    private String userId;
    private String textContent;
    private String picContent;
    private Date createTime;
    private double longitude; //经度
    private double latitude; //纬度
    private String location;//为了不重复用经纬度去获取地名
    private Date updateTime;//记录评论的最后更新时间
    private String nickName;
    private String portrait;


    public MomentBean() {
    }

    public MomentBean(Moment moment, String nickName, String portrait) {
        new MomentBean(moment.getMomentId(), moment.getUserId(), moment.getTextContent(), moment.getPicContent(), moment.getCreateTime(), moment.getLongitude(), moment.getLatitude(), moment.getLocation(), moment.getUpdateTime(), nickName, portrait);
    }


    public MomentBean(String momentId, String userId, String textContent, String picContent, Date createTime, double longitude, double latitude, String location, Date updateTime, String nickName, String portrait) {
        this.momentId = momentId;
        this.userId = userId;
        this.textContent = textContent;
        this.picContent = picContent;
        this.createTime = createTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
        this.updateTime = updateTime;
        this.nickName = nickName;
        this.portrait = portrait;
    }

    @Override
    public String toString() {
        return "MomentBean{" +
                "momentId='" + momentId + '\'' +
                ", userId='" + userId + '\'' +
                ", textContent='" + textContent + '\'' +
                ", picContent='" + picContent + '\'' +
                ", createTime=" + createTime +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", location='" + location + '\'' +
                ", updateTime=" + updateTime +
                ", nickName='" + nickName + '\'' +
                ", portrait='" + portrait + '\'' +
                '}';
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
