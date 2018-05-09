package com.scut.weixinserver.controller;


import com.scut.weixinserver.entity.Moment;
import com.scut.weixinserver.model.MomentReq;
import com.scut.weixinserver.model.Result;
import com.scut.weixinserver.model.ResultCode;
import com.scut.weixinserver.service.MomentService;
import com.scut.weixinserver.utils.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping(path="/moment")
public class MomentController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${img.picContentLocation}")
    private String picContentLocation;

    @Autowired
    private MomentService momentService;


    @PostMapping(path="/nearby")
    public @ResponseBody
    ResponseEntity getNearby(@RequestBody MomentReq momentReq) {
        return momentService.getMomentsNearby(momentReq.getLatitude(), momentReq.getLongitude(),
                momentReq.getPageNum(), momentReq.getPageSize());
    }

    @PostMapping(path="/detail")
    public @ResponseBody
    ResponseEntity getMomentByNearby(@RequestBody MomentReq momentReq) {
        List<String> idList = Arrays.asList(momentReq.getIds().split(","));
        return momentService.getMomentsByMomentId(idList);
    }


    @PostMapping(path="/personal")
    public @ResponseBody
    ResponseEntity getMomentByUserId(@RequestBody MomentReq momentReq) {
        return momentService.getMomentsByUserId(momentReq.getUserId(), momentReq.getPageNum(), momentReq.getPageSize());
    }

    @PostMapping(path="/create")
    public @ResponseBody
    ResponseEntity createMoment(@RequestBody Moment moment, HttpServletRequest request) {
//        moment.setUserId(request.getAttribute("userId").toString());
        return momentService.createMoment(moment);
    }

    @PostMapping(path="/piccontent")
    public @ResponseBody
    ResponseEntity uploadPicContent(HttpServletRequest request, String momentId ) {
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("picContent");
        if(files.size() == 0 ) {
            Result<String> result = new Result<>();
            result.setCodeAndMsg(ResultCode.IMG_NOT_ALLOW);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            List<String> fileUrls = new ArrayList<>();
            for (int i = 0; i < files.size(); i++) {
                try {
                    fileUrls.add("/piccontent/" + ImageUtil.saveImg(files.get(i), picContentLocation));
                } catch (IOException e) {
                    e.printStackTrace();
                    Result<String> result = new Result<>();
                    result.setCodeAndMsg(ResultCode.IMG_NOT_ALLOW);
                    return new ResponseEntity<>(result,HttpStatus.OK);
                }
            }
            return momentService.uploadPicContent(momentId, fileUrls);
        }
    }


    @PostMapping(path="/delete")
    public @ResponseBody
    ResponseEntity deleteMoment(@RequestBody Moment moment) {
        return momentService.deleteMoment(moment);
    }
}
