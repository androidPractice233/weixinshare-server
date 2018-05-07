package com.scut.weixinserver.controller;


import com.scut.weixinserver.entity.User;
import com.scut.weixinserver.model.Result;
import com.scut.weixinserver.model.ResultCode;
import com.scut.weixinserver.service.UserService;
import com.scut.weixinserver.utils.ImageUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(path="/user")
//login和register都可在没验证token的情况下访问，login会返回生成的token
public class UserController {


    @Value("${img.portraitLocation}")
    private String portraitLocation;

    @Autowired
    private UserService userService;

    @PostMapping(path = "/login")
    public ResponseEntity token(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping(path="/register")
    public @ResponseBody
    ResponseEntity register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping(path="/portrait")
    public @ResponseBody
    ResponseEntity setPortrait(@RequestBody MultipartFile portrait,
                               HttpServletRequest request) {

        if(portrait.isEmpty() || "".equals(portrait.getOriginalFilename())) {
            Result<String> result = new Result<>();
            result.setCodeAndMsg(ResultCode.IMG_NOT_ALLOW);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }else {
            String userId = request.getAttribute("userId").toString();
            String portraitUrl;
            try{
                portraitUrl = ImageUtil.saveImg(portrait, portraitLocation);
                return userService.setPortrait(userId, "/portrait/" + portraitUrl);
            } catch (IOException e) {
                e.printStackTrace();
                Result<String> result = new Result<>();
                result.setCodeAndMsg(ResultCode.IMG_NOT_ALLOW);
                return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }


    @PostMapping(path="/update")
    public @ResponseBody
    ResponseEntity update(@RequestBody User user, HttpServletRequest request) {
        String userId = request.getAttribute("userId").toString();
        user.setUserId(userId);
        return userService.update(user);
    }

    @PostMapping(path="/delete")
    public @ResponseBody
    ResponseEntity delete(@RequestBody User user, HttpServletRequest request) {
        String userId = request.getAttribute("userId").toString();
        user.setUserId(userId);
        return userService.delete(user);
    }

    @PostMapping(path="/search")
    public @ResponseBody
    ResponseEntity search(@RequestBody User user) {
        return userService.search(user);
    }

    @PostMapping(path="/getnickpot")
    public @ResponseBody
    ResponseEntity getnickpot(@RequestBody String userIds) {
        List<String> idList = Arrays.asList(userIds.split(","));
        return userService.getNickPot(idList);
    }


}
