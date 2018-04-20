package com.scut.weixinserver.service;


import com.scut.weixinserver.entity.Comment;
import com.scut.weixinserver.entity.Moment;
import com.scut.weixinserver.model.Result;
import com.scut.weixinserver.model.ResultCode;
import com.scut.weixinserver.repo.CommentRepository;
import com.scut.weixinserver.repo.MomentRepository;
import com.scut.weixinserver.repo.UserRepository;
import com.scut.weixinserver.utils.MapUtil;
import com.scut.weixinserver.utils.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    public ResponseEntity getMomentsNearby(double latitude, double longitude,
                                           int pageNum, int pageSize) {
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

        PageRequest pageRequest = new PageRequest(pageNum, pageSize,
                new Sort(Sort.Direction.DESC, "createTime"));
        List<Moment> moments = momentRepository.getMomentsByLatitudeBetweenAndLongitudeBetween(
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
        if(resultList.size() > 0) {
            result.setData(resultList);
            result.setCodeAndMsg(ResultCode.SUCCESS);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            result.setCodeAndMsg(ResultCode.MOMENT_NOT_UPDATE);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }
    }


    public ResponseEntity getMomentsByMomentId(List ids) {
        Result<List<List<Object>>> result = new Result<>();
        if(ids.size() == 0) {
            result.setCodeAndMsg(ResultCode.MOMENT_NOT_UPDATE);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }else {
            List<Moment> moments = momentRepository.getMomentsByMomentIdIn(ids);
            List<List<Object>> resultMoments = new ArrayList<>();
            for (Moment moment : moments) {
                List<Object> temp = new ArrayList<>();
                temp.add(moment);
                List<Comment> comments = commentRepository.getCommentsByMomentId(moment.getMomentId());
                if (comments.size() > 0) {
                    temp.addAll(comments);
                }
                resultMoments.add(temp);
            }
            if(resultMoments.size() > 0) {
                result.setData(resultMoments);
                result.setCodeAndMsg(ResultCode.SUCCESS);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }else {
                result.setCodeAndMsg(ResultCode.MOMENT_NOT_UPDATE);
                return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    public ResponseEntity getMomentsByUserId(String userId, int pageNum, int pageSize) {
        Result<List<List<Object>>> result = new Result<>();

        PageRequest pageRequest = new PageRequest(pageNum, pageSize,
                new Sort(Sort.Direction.DESC, "createTime"));
        List<Moment> moments = momentRepository.getMomentsByUserId(userId, pageRequest);

        List<List<Object>> resultMoments = new ArrayList<>();
        for (Moment moment : moments) {
            //查询结果，每个子list第一条为moment， 后面为comment
            List<Object> temp = new ArrayList<>();
            temp.add(moment);
            List<Comment> comments = commentRepository.getCommentsByMomentId(moment.getMomentId());
            if (comments.size() > 0) {
                temp.addAll(comments);
            }
            resultMoments.add(temp);
        }
        if(resultMoments.size() > 0) {
            result.setData(resultMoments);
            result.setCodeAndMsg(ResultCode.SUCCESS);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            result.setCodeAndMsg(ResultCode.MOMENT_NOT_UPDATE);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public ResponseEntity createMoment(Moment moment) {
        Result<Map> result = new Result<>();
        moment.setMomentId(Uuid.getUuid());
        moment.setCreateTime(new Date());
        moment.setUpdateTime(new Date());
        moment = momentRepository.save(moment);
        if(moment.getMomentId() == null || moment.getMomentId().equals("")){
            throw new RuntimeException("UserServices: insert user wrong");
        }
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("momentId", moment.getMomentId());
        result.setData(tempMap);
        result.setCodeAndMsg(ResultCode.SUCCESS);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity uploadPicContent(String momentId, List<String> fileUrls) {
        Result<Map> result = new Result<>();
        Moment momentFromDb = momentRepository.getMomentByMomentId(momentId);
        if(momentFromDb == null) {
            result.setCodeAndMsg(ResultCode.MOMENT_NOT_EXIST);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }else {
            String url = String.join(",", fileUrls);
            momentFromDb.setPicContent(url);
            momentRepository.save(momentFromDb);
            Map<String, String> temp = new HashMap<>();
            temp.put("picContent", url);
            result.setData(temp);
            result.setCodeAndMsg(ResultCode.SUCCESS);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity deleteMoment(Moment moment) {
        Result<String> result = new Result<>();
        int n = momentRepository.deleteMomentByMomentId(moment.getMomentId());
        if(n == 0) {
            result.setCodeAndMsg(ResultCode.MOMENT_NOT_EXIST);
        } else {
            commentRepository.deleteCommentsByMomentId(moment.getMomentId());
            result.setCodeAndMsg(ResultCode.SUCCESS);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity createComment(Comment comment) {
        Result<Map> result = new Result<>();
        comment.setCommentId(Uuid.getUuid());
        comment.setCreateTime(new Date());
        comment = commentRepository.save(comment);
        if(comment.getMomentId() == null || comment.getMomentId().equals("")){
            throw new RuntimeException("UserServices: insert moment wrong");
        }
        Moment moment = momentRepository.getMomentByMomentId(comment.getMomentId());
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
        Result<String> result = new Result<>();
        if(comment.getCommentId() == null || comment.getCommentId().equals("")){
            result.setCodeAndMsg(ResultCode.COMMENT_NOT_EXIST);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }
        comment = commentRepository.getCommentByCommentId(comment.getCommentId());
        int n = commentRepository.deleteCommentByCommentId(comment.getCommentId());
        if(n == 0) {
            result.setCodeAndMsg(ResultCode.COMMENT_NOT_EXIST);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        } else {
            result.setCodeAndMsg(ResultCode.SUCCESS);
        }
        Moment moment = momentRepository.getMomentByMomentId(comment.getMomentId());
        moment.setUpdateTime(new Date());
        momentRepository.save(moment);
        result.setCodeAndMsg(ResultCode.SUCCESS);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity updateComment(String userId, long sinceTime,
                                        int pageNum, int pageSize) {
        Result<List<Comment>> result = new Result<>();
        List<Comment> resultComments = new ArrayList<>();
        PageRequest pageRequest = new PageRequest(pageNum, pageSize,
                new Sort(Sort.Direction.DESC, "createTime"));
        List<Moment> moments = momentRepository.getMomentsByUserId(userId, pageRequest);
        for (Moment moment : moments) {
            List<Comment> comments = commentRepository.getCommentByMomentIdAndCreateTimeAfter(
                    moment.getMomentId(), new Date(sinceTime));

            if (comments.size() > 0) {
                resultComments.addAll(comments);
            }
        }
        if(resultComments.size() > 0) {
            result.setData(resultComments);
            result.setCodeAndMsg(ResultCode.SUCCESS);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            result.setCodeAndMsg(ResultCode.COMMENT_NOT_UPDATE);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
