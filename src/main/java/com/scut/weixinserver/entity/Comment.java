package com.scut.weixinserver.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Comment {

    @Id
    private String commentId;
    private String momentId;
    private String sendId;
    private String recvId;
    private String content;
    private Date createTime;


    public Comment() {
    }

    public Comment(String commentId, String momentId, String sendId, String recvId, String content, Date createTime) {
        this.commentId = commentId;
        this.momentId = momentId;
        this.sendId = sendId;
        this.recvId = recvId;
        this.content = content;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", momentId='" + momentId + '\'' +
                ", sendId='" + sendId + '\'' +
                ", recvId='" + recvId + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getRecvId() {
        return recvId;
    }

    public void setRecvId(String recvId) {
        this.recvId = recvId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
