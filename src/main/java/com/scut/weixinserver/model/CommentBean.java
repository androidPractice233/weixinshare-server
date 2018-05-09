package com.scut.weixinserver.model;

import com.scut.weixinserver.entity.Comment;

import java.util.Date;

public class CommentBean {

    private String commentId;
    private String momentId;
    private String sendId;
    private String recvId;
    private String content;
    private Date createTime;
    private String sendNickName;
    private String sendPortrait;
    private String recvNickName;


    public CommentBean() {
    }

    public static CommentBean getCommentBean(Comment comment, String sendNickName, String sendPortrait, String recvNickName) {
        return new CommentBean(comment.getCommentId(), comment.getMomentId(), comment.getSendId(), comment.getRecvId(), comment.getContent(), comment.getCreateTime(), sendNickName, sendPortrait, recvNickName);
    }
    public CommentBean(String commentId, String momentId, String sendId, String recvId, String content, Date createTime, String sendNickName, String sendPortrait, String recvNickName) {
        this.commentId = commentId;
        this.momentId = momentId;
        this.sendId = sendId;
        this.recvId = recvId;
        this.content = content;
        this.createTime = createTime;
        this.sendNickName = sendNickName;
        this.sendPortrait = sendPortrait;
        this.recvNickName = recvNickName;
    }

    @Override
    public String toString() {
        return "CommentBean{" +
                "commentId='" + commentId + '\'' +
                ", momentId='" + momentId + '\'' +
                ", sendId='" + sendId + '\'' +
                ", recvId='" + recvId + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", sendNickName='" + sendNickName + '\'' +
                ", sendPortrait='" + sendPortrait + '\'' +
                ", recvNickName='" + recvNickName + '\'' +
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

    public String getSendNickName() {
        return sendNickName;
    }

    public void setSendNickName(String sendNickName) {
        this.sendNickName = sendNickName;
    }

    public String getSendPortrait() {
        return sendPortrait;
    }

    public void setSendPortrait(String sendPortrait) {
        this.sendPortrait = sendPortrait;
    }

    public String getRecvNickName() {
        return recvNickName;
    }

    public void setRecvNickName(String recvNickName) {
        this.recvNickName = recvNickName;
    }



}
