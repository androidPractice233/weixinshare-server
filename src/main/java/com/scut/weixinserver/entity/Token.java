package com.scut.weixinserver.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Token {

    @Id
    private String tokenId;
    private String userId;
    private String token;
    private String buildTime;

    public Token() {
    }

    public Token(String tokenId, String userId, String token, String buildTime) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.token = token;
        this.buildTime = buildTime;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenId='" + tokenId + '\'' +
                ", userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                ", buildTime='" + buildTime + '\'' +
                '}';
    }
}
