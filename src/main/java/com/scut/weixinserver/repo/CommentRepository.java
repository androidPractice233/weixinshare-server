package com.scut.weixinserver.repo;

import com.scut.weixinserver.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findCommentByCommentId(String commentId);

    int deleteCommentsBySendId(String senderId);

    int deleteCommentByCommentId(String commentId);

    int deleteCommentsByMomentId(String momentId);

    List<Comment> findCommentsByMomentId(String momentId);

    List<Comment> findCommentsByRecvIdAndCreateTimeAfter(String recvId, Date createTime);

    List<Comment> findCommentsByMomentIdAndCreateTimeAfter(String momentId, Date createTime);
}
