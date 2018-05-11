package com.scut.weixinserver.model;

public class UserReq {

    private String userIds;

    public UserReq() {
    }

    @Override
    public String toString() {
        return "UserReq{" +
                "userIds='" + userIds + '\'' +
                '}';
    }

    public UserReq(String userIds) {
        this.userIds = userIds;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }
}
