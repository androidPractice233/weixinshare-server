package com.scut.weixinserver.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class User {
    @Id
    private String userId;
    private String userName;
    private String nickName;
    private String userPwd;
    private int sex;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String portrait;
    private Date lastLoginTime;
    private String location; //位置


    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", portrait='" + portrait + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", location='" + location + '\'' +
                '}';
    }

    public User(String userId, String userName, String nickName, String userPwd, int sex, Date birthday, String portrait, Date lastLoginTime, String location) {
        this.userId = userId;
        this.userName = userName;
        this.nickName = nickName;
        this.userPwd = userPwd;
        this.sex = sex;
        this.birthday = birthday;
        this.portrait = portrait;
        this.lastLoginTime = lastLoginTime;
        this.location = location;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

}
