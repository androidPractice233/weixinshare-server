package com.scut.weixinserver.controller;


import com.scut.weixinserver.entity.Comment;
import com.scut.weixinserver.model.CommentReq;
import com.scut.weixinserver.service.MomentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(path="/comment")
public class CommentController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MomentService momentService;

    @PostMapping(path="/create")
    public @ResponseBody
    ResponseEntity createComment(@RequestBody Comment comment, HttpServletRequest request) {
    	logger.info("inteceptor后request中是否存有userid:"+request.getAttribute("userId"));
       comment.setSendId(request.getAttribute("userId").toString());
        return momentService.createComment(comment);
    }

    @PostMapping(path="/delete")
    public @ResponseBody
    ResponseEntity deleteComment(@RequestBody Comment comment) {
        return momentService.deleteComment(comment);
    }

    @PostMapping(path="/pull")
    public @ResponseBody
    //userId在Interpretor中已经设置到request中了
    ResponseEntity updateComment(@RequestBody CommentReq commentReq, HttpServletRequest request) {
//        String userId = request.getAttribute("userId").toString();
        return momentService.updateComment(commentReq.getUserId(), commentReq.getDateTime(),
                commentReq.getPageNum(), commentReq.getPageSize());
    }




}

