package com.scut.weixinserver.service;


import com.scut.weixinserver.entity.Comment;
import com.scut.weixinserver.entity.Moment;
import com.scut.weixinserver.entity.User;
import com.scut.weixinserver.model.CommentBean;
import com.scut.weixinserver.model.MomentBean;
import com.scut.weixinserver.model.Result;
import com.scut.weixinserver.model.ResultCode;
import com.scut.weixinserver.repo.CommentRepository;
import com.scut.weixinserver.repo.MomentRepository;
import com.scut.weixinserver.repo.UserRepository;
import com.scut.weixinserver.utils.MapUtil;
import com.scut.weixinserver.utils.Uuid;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MomentService {
    @Autowired
    private MomentRepository momentRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public ResponseEntity getMomentsNearby(double latitude, double longitude,
                                           int pageNum, int pageSize) {
        logger.info("MomentService.getMomentsNearby: args latitude={}, longitude={}, pageNum={}, pageSize={}", latitude, longitude, pageNum, pageSize);
        Result<List<Map>> result = new Result<>();
        //范围5km
        double minLongitude, maxLongitude, minLatitude, maxLatitude;
        //先用一定范围筛选出可能符合限制的动态，再通过计算距离进一步筛选，
        //避免对每一个动态都进行距离计算，效率太低
        //纬度1度大致对应111km
        minLatitude = latitude - (5.0/111.0);
        maxLatitude = latitude + (5.0/111.0);
        //经度1度对应距离与纬度相关
        minLongitude = longitude - (5.0/(111.0*Math.cos(latitude)));
        maxLongitude = longitude + (5.0/(111.0*Math.cos(latitude)));
        //最小最大经度有可能倒置了
        if(minLongitude > maxLongitude) {
            double temp = minLongitude;
            minLongitude = maxLongitude;
            maxLongitude = temp;
        }
        PageRequest pageRequest = new PageRequest(pageNum, pageSize,
                new Sort(Sort.Direction.DESC, "createTime"));

        List<Moment> moments = momentRepository.findMomentsByLatitudeBetweenAndLongitudeBetweenOrderByCreateTimeDesc(
                minLatitude, maxLatitude, minLongitude, maxLongitude, pageRequest);
        List<Map> resultList = new ArrayList<>();
        for (Moment moment : moments) {
            Map<String, Object> tempMap = new HashMap<>();
            if (MapUtil.getDistance(latitude, longitude,
                    moment.getLatitude(), moment.getLongitude())
                    <= 5000) {
                tempMap.put("momentId", moment.getMomentId());
                tempMap.put("updateTime", moment.getUpdateTime());
                resultList.add(tempMap);
            }
        }
        logger.info("MomentService.getMomentsNearby: resultListSize={}", resultList.size());
        if(resultList.size() > 0) {
            result.setData(resultList);
            result.setCodeAndMsg(ResultCode.SUCCESS);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            result.setCodeAndMsg(ResultCode.MOMENT_NOT_UPDATE);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity getMomentsByMomentId(List ids) {
        logger.info("MomentService.getMomentsByMomentId:args={}", ids.toString());
        Result<List<List<Object>>> result = new Result<>();
        if(ids.size() == 0) {
            result.setCodeAndMsg(ResultCode.MOMENT_NOT_UPDATE);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            List<Moment> moments = momentRepository.findMomentsByMomentIdIn(ids);
            List<List<Object>> resultMoments = new ArrayList<>();
            for (Moment moment : moments) {
                List<Object> temp = new ArrayList<>();
                User user = userRepository.findUserByUserId(moment.getUserId());
                if(user == null) {
                    momentRepository.deleteMomentByMomentId(moment.getMomentId());
                    commentRepository.deleteCommentsByMomentId(moment.getMomentId());
                    continue;
                }
                temp.add(MomentBean.getMomentBean(moment, user.getNickName(), user.getPortrait()));
                List<Comment> comments = commentRepository.findCommentsByMomentId(moment.getMomentId());
                List<CommentBean> commentBeans = new ArrayList<>();
                for(Comment comment : comments) {
                    User sender = userRepository.findUserByUserId(comment.getSendId());
                    User recver = userRepository.findUserByUserId(comment.getRecvId());
                    if(sender == null) {
                        logger.info("MomentService.getMomentsByMomentId:sender not found.commentId:{}, sendId:{}, recvId:{}", comment.getCommentId(),
                                comment.getSendId(), comment.getRecvId());
                    }else if(recver == null) {
                        commentBeans.add(CommentBean.getCommentBean(comment, sender.getNickName(), sender.getPortrait(), null));
                    }else {
                        commentBeans.add(CommentBean.getCommentBean(comment, sender.getNickName(), sender.getPortrait(), recver.getNickName()));
                    }
                }
                if (commentBeans.size() > 0) {
                    temp.addAll(commentBeans);
                }
                resultMoments.add(temp);
            }
            logger.info("MomentService.getMomentsByMomentId: return size = {}", resultMoments.size());
            if(resultMoments.size() > 0) {
                result.setData(resultMoments);
                result.setCodeAndMsg(ResultCode.SUCCESS);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }else {
                result.setCodeAndMsg(ResultCode.MOMENT_NOT_UPDATE);
                return new ResponseEntity<>(result,HttpStatus.OK);
            }
        }
    }

    public ResponseEntity getMomentsByUserId(String userId, int pageNum, int pageSize) {
        logger.info("MomentService.getMomentsByUserId: userId = {}, pageNum = {}, pageSize = {}", userId, pageNum, pageSize);    
        PageRequest pageRequest = new PageRequest(pageNum, pageSize,
                new Sort(Sort.Direction.DESC, "createTime"));
        List<Moment> moments = momentRepository.findMomentsByUserIdOrderByCreateTimeDesc(userId, pageRequest);
        List<Map> resultList = new ArrayList<>();
        Result<List<Map>> result = new Result<>();
//        List<List<Object>> resultMoments = new ArrayList<>();
//        for (Moment moment : moments) {
//            //查询结果，每个子list第一条为moment， 后面为comment
//            List<Object> temp = new ArrayList<>();
//            User user = userRepository.findUserByUserId(moment.getUserId());
//            
//            temp.add(MomentBean.getMomentBean(moment, user.getNickName(), user.getPortrait()));
//            List<Comment> comments = commentRepository.findCommentsByMomentId(moment.getMomentId());
//            List<CommentBean> commentBeans = new ArrayList<>();
//            for(Comment comment : comments) {
//                User sender = userRepository.findUserByUserId(comment.getSendId());
//                User recver = userRepository.findUserByUserId(comment.getRecvId());
//                if(sender == null || recver == null) {
//                    logger.info("MomentService.getMomentsByUserId:sender or recver not found.commentId:{}, sendId:{}, recvId:{}", comment.getCommentId(),
//                            comment.getSendId(), comment.getRecvId());
//                } else {
//                    commentBeans.add(CommentBean.getCommentBean(comment, sender.getNickName(), sender.getPortrait(), recver.getNickName()));
//                }
//            }
//            if (commentBeans.size() > 0) {
//                temp.addAll(commentBeans);
//            }
//            resultMoments.add(temp);
            
            for (Moment moment : moments) {
                Map<String, Object> tempMap = new HashMap<>();  
                    tempMap.put("momentId", moment.getMomentId());
                    tempMap.put("updateTime", moment.getUpdateTime());
                    resultList.add(tempMap);
                }
        
        logger.info("MomentService.getMomentsByUserId: return size = {}", resultList.size());
        if(resultList.size() > 0) {
            result.setData(resultList);
            result.setCodeAndMsg(ResultCode.SUCCESS);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            result.setCodeAndMsg(ResultCode.MOMENT_NOT_UPDATE);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }
    public ResponseEntity createMoment(Moment moment) {
        logger.info("MomentService.createMoment: args={}", moment.toString());
        Result<Map> result = new Result<>();
        moment.setMomentId(Uuid.getUuid());
        moment.setCreateTime(new Date());
        moment.setUpdateTime(new Date());
        moment = momentRepository.save(moment);
        if(moment.getMomentId() == null || moment.getMomentId().equals("")){
            logger.error("MomentService.createMoment: insert error={}", moment.toString());
            result.setCodeAndMsg(ResultCode.SERVER_ERROR);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("momentId", moment.getMomentId());
        result.setData(tempMap);
        result.setCodeAndMsg(ResultCode.SUCCESS);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity uploadPicContent(String momentId, List<String> fileUrls) {
        logger.info("MomentService.uploadPicContent: momentId={}", momentId);
        Result<Map> result = new Result<>();
        Moment momentFromDb = momentRepository.findMomentByMomentId(momentId);
        if(momentFromDb == null) {
            logger.info("MomentService.uploadPicContent: momentNotFound momentId={}", momentId);
            result.setCodeAndMsg(ResultCode.MOMENT_NOT_EXIST);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            String url = Strings.join(fileUrls, ',');
            momentFromDb.setPicContent(url);
            momentFromDb.setUpdateTime(new Date());
            momentRepository.save(momentFromDb);
            logger.info("MomentService.uploadPicContent: save success, url={}", url);
            Map<String, String> temp = new HashMap<>();
            temp.put("picContent", url);
            result.setData(temp);
            result.setCodeAndMsg(ResultCode.SUCCESS);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity deleteMoment(Moment moment) {
        logger.info("MomentService.deleteMoment: args={}", moment.toString());
        Result<String> result = new Result<>();
        int n = momentRepository.deleteMomentByMomentId(moment.getMomentId());
        if(n == 0) {
            logger.info("MomentService.deleteMoment: momentNotFound moment={}", moment.toString());
            result.setCodeAndMsg(ResultCode.MOMENT_NOT_EXIST);
        } else {
            commentRepository.deleteCommentsByMomentId(moment.getMomentId());
            result.setCodeAndMsg(ResultCode.SUCCESS);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity createComment(Comment comment) {
        logger.info("MomentService.createComment: comment={}", comment.toString());
        Result<Map> result = new Result<>();
        comment.setCommentId(Uuid.getUuid());
        comment.setCreateTime(new Date());
        comment = commentRepository.save(comment);
        if(comment.getMomentId() == null || comment.getMomentId().equals("")){
            logger.error("MomentService.createComment: insert error={}", comment.toString());
            result.setCodeAndMsg(ResultCode.SERVER_ERROR);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        Moment moment = momentRepository.findMomentByMomentId(comment.getMomentId());
        moment.setUpdateTime(comment.getCreateTime());
        momentRepository.save(moment);
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("commentId", comment.getCommentId());
        result.setData(tempMap);
        result.setCodeAndMsg(ResultCode.SUCCESS);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity deleteComment(Comment comment) {
        logger.info("MomentService.deleteComment: comment={}", comment.toString());
        Result<String> result = new Result<>();
        if(comment.getCommentId() == null || comment.getCommentId().equals("")){
            result.setCodeAndMsg(ResultCode.COMMENT_NOT_EXIST);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }
        int n = commentRepository.deleteCommentByCommentId(comment.getCommentId());
        if(n == 0) {
            logger.info("MomentService.deleteComment: comentNotFound comentId={}", comment.toString());
            result.setCodeAndMsg(ResultCode.COMMENT_NOT_EXIST);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setCodeAndMsg(ResultCode.SUCCESS);
        }
        Moment moment = momentRepository.findMomentByMomentId(comment.getMomentId());
        moment.setUpdateTime(new Date());
        momentRepository.save(moment);
        result.setCodeAndMsg(ResultCode.SUCCESS);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity updateComment(String userId, long sinceTime,
                                        int pageNum, int pageSize) {
        logger.info("MomentService.updateComment: userId={}, sinceTime={}, pageNum={}, pageSize={}", userId, sinceTime, pageNum, pageSize);
        Result<List<Comment>> result = new Result<>();
        List<Comment> resultComments = new ArrayList<>();
        PageRequest pageRequest = new PageRequest(pageNum, pageSize,
                new Sort(Sort.Direction.DESC, "createTime"));
        List<Moment> moments = momentRepository.findMomentsByUserIdOrderByCreateTimeDesc(userId, pageRequest);
        Set<String> momentIdSet = new HashSet<>();
        for (Moment moment : moments) {
            momentIdSet.add(moment.getMomentId());
            List<Comment> comments = commentRepository.findCommentsByMomentIdAndCreateTimeAfter(
                    moment.getMomentId(), new Date(sinceTime));
            if (comments.size() > 0) {
                resultComments.addAll(comments);
            }
        }
        //在其他人动态下的评论更新
        List<Comment> commentsFromOtherMoment = commentRepository.findCommentsByRecvIdAndCreateTimeAfter(userId, new Date(sinceTime));
        for(Comment comment:commentsFromOtherMoment) {
            //不包含在已拿到comment里
            if(!momentIdSet.contains(comment.getMomentId())) {
                resultComments.add(comment);
            }
        }


        if(resultComments.size() > 0) {
            resultComments.sort(new Comparator<Comment>() {
                @Override
                public int compare(Comment comment, Comment t1) {
                    return t1.getCreateTime().compareTo(comment.getCreateTime());
                }
            });
            result.setData(resultComments);
            result.setCodeAndMsg(ResultCode.SUCCESS);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            result.setCodeAndMsg(ResultCode.COMMENT_NOT_UPDATE);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

}
