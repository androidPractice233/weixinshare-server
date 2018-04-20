package com.scut.weixinserver.repo;

import com.scut.weixinserver.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment getCommentByCommentId(String commentId);

    int deleteCommentsBySendId(String senderId);

    int deleteCommentByCommentId(String commentId);

    int deleteCommentsByMomentId(String momentId);

    List<Comment> getCommentsByMomentId(String momentId);

    List<Comment> getCommentByMomentIdAndCreateTimeAfter(String momentId, Date createTime);
}
