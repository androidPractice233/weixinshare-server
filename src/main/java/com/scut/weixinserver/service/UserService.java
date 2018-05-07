package com.scut.weixinserver.service;

import com.scut.weixinserver.entity.Token;
import com.scut.weixinserver.entity.User;
import com.scut.weixinserver.model.Result;
import com.scut.weixinserver.model.ResultCode;
import com.scut.weixinserver.repo.CommentRepository;
import com.scut.weixinserver.repo.MomentRepository;
import com.scut.weixinserver.repo.TokenRepository;
import com.scut.weixinserver.repo.UserRepository;
import com.scut.weixinserver.utils.MD5;
import com.scut.weixinserver.utils.Uuid;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Value("${img.portraitLocation}")
    private String portraitLocation;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private CommentRepository commentRepository;


    @Autowired
    private MomentRepository momentRepository;

    @Autowired
    private TokenRepository tokenRepository;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ResponseEntity login(User user) {
        logger.info("UserService.login: args={}", user.toString());
        Result<String> result = new Result<>();

        if(user.getUserId() == null || "".equals(user.getUserId())) {
            result.setCodeAndMsg(ResultCode.USER_ID_EXIST);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        } else if(user.getUserPwd() == null || "".equals(user.getUserPwd())) {
            result.setCodeAndMsg(ResultCode.USER_PASS_ERR);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        } else {
            User userFromDb = userRepository.findUserByUserId(user.getUserId());
            user.setUserPwd(MD5.MD5Encode(user.getUserPwd(), "UTF-8"));
            if(userFromDb == null) {
                logger.info("UserService.login: userNotFound={}", user.toString());
                result.setCodeAndMsg(ResultCode.USER_NOT_EXIST);
                return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
            } else if(!userFromDb.getUserPwd().equals(user.getUserPwd())) {
                logger.info("UserService.login: userPassErr={}", user.toString());
                result.setCodeAndMsg(ResultCode.USER_PASS_ERR);
                return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
            } else {
                //更新登录时间
                userFromDb.setLastLoginTime(new Date());
                userRepository.save(userFromDb);

                Token tokenFromDb = tokenRepository.findTokenByUserId(user.getUserId());
                String tokenStr;
                if(tokenFromDb == null) {
                    tokenStr = createNewToken(user.getUserId());
                    tokenFromDb = new Token();
                    tokenFromDb.setTokenId(Uuid.getUuid());
                    tokenFromDb.setUserId(user.getUserId());
                    tokenFromDb.setBuildTime(String.valueOf(System.currentTimeMillis()));
                    tokenFromDb.setToken(tokenStr);
                    logger.info("UserService.login: gen new Token, user={}, token={}", user.toString(), tokenStr);
                    tokenRepository.save(tokenFromDb);
                } else {
                    long builtTime = Long.valueOf(tokenFromDb.getBuildTime());
                    long currentTime = System.currentTimeMillis();
                    long second = TimeUnit.MILLISECONDS.toSeconds(currentTime - builtTime);
                    //10天的有效期
                    if(second > 0 && second < 864000) {
                        tokenStr = tokenFromDb.getToken();
                        logger.info("UserService.login: tokenInTime={}", tokenStr);
                    } else {
                        tokenStr = createNewToken(user.getUserId());
                        tokenFromDb.setBuildTime(String.valueOf(System.currentTimeMillis()));
                        tokenFromDb.setToken(tokenStr);
                        logger.info("UserService.login: tokenOutOfTime, genNewToken={}", tokenStr);
                        tokenRepository.save(tokenFromDb);
                    }
                }
                result.setData(tokenStr);
                result.setCodeAndMsg(ResultCode.SUCCESS);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }
    }

    public ResponseEntity register(User user) {
        logger.info("UserService.register: user={}", user.toString());
        Result<Map> result = new Result<>();
        if("".equals(user.getUserName())) {
            result.setCodeAndMsg(ResultCode.USER_NAME_ERR);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }else if("".equals(user.getUserPwd())){
            result.setCodeAndMsg(ResultCode.USER_PASS_ERR);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }else {
            user.setUserId(Uuid.getUuid());
            user.setLastLoginTime(new Date());
            user.setUserPwd(MD5.MD5Encode(user.getUserPwd(), "UTF-8"));
            user = userRepository.save(user);
            if(user.getUserId() == null || user.getUserId().equals("")) {
                logger.error("UserService.register: insert error={}", user.toString());
                result.setCodeAndMsg(ResultCode.SERVER_ERROR);
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Map temp = new HashMap();
            temp.put("userId", user.getUserId());
            result.setCodeAndMsg(ResultCode.SUCCESS);
            result.setData(temp);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }


    public ResponseEntity setPortrait(String userId, String portraitUrl) {
        logger.info("UserService.setPortrait: userId={}, portrait={}", userId, portraitUrl);
        Result<Map> result = new Result<>();
        User userFromDb = userRepository.findUserByUserId(userId);
        if(userFromDb == null) {
            logger.info("UserService.setPortrait: userNotFound={}", userId);
            result.setCodeAndMsg(ResultCode.USER_ID_EXIST);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }else {
            userFromDb.setPortrait(portraitUrl);
            userRepository.save(userFromDb);
            Map<String, String> temp = new HashMap<>();
            temp.put("portrait", portraitUrl);
            result.setData(temp);
            result.setCodeAndMsg(ResultCode.SUCCESS);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }


    public ResponseEntity update(User user) {
        logger.info("UserService.update: user={}", user.toString());
        Result<String> result = new Result<>();

        if(user.getUserId() == null || "".equals(user.getUserId())) {
            logger.info("UserService.update: invalidParams={}", user.toString());
            result.setCodeAndMsg(ResultCode.USER_NOT_EXIST);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        } else if(user.getUserPwd() == null || "".equals(user.getUserPwd())) {
            logger.info("UserService.update: userPassErr={}", user.toString());
            result.setCodeAndMsg(ResultCode.USER_PASS_ERR);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        } else {
            User userFromDb = userRepository.findUserByUserId(user.getUserId());
            user.setUserPwd(MD5.MD5Encode(user.getUserPwd(), "UTF-8"));
            if(userFromDb == null) {
                logger.info("UserService.update: userNotFound={}", user.toString());
                result.setCodeAndMsg(ResultCode.USER_NOT_EXIST);
                return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
            } else {
                user.setPortrait(userFromDb.getPortrait());
                user.setLastLoginTime(userFromDb.getLastLoginTime());
                userRepository.save(user);
                result.setCodeAndMsg(ResultCode.SUCCESS);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }
    }

    @Transactional
    public ResponseEntity delete(User user) {
        logger.info("UserService.delete: user={}", user.toString());
        Result<String> result = new Result<>();

        if(user.getUserId() == null || "".equals(user.getUserId())) {
            logger.info("UserService.delete: invalidParams={}", user.toString());
            result.setCodeAndMsg(ResultCode.USER_NOT_EXIST);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        } else {
            int n ;
            n = userRepository.deleteUserByUserId(user.getUserId());
            if(n != 0) {
                //需要删除头像和图片文件？
                momentRepository.deleteMomentsByUserId(user.getUserId());
                commentRepository.deleteCommentsBySendId(user.getUserId());
                tokenRepository.deleteTokenByUserId(user.getUserId());
                result.setCodeAndMsg(ResultCode.SUCCESS);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }else{
                logger.info("UserService.delete: userNotFound={}", user.toString());
                result.setCodeAndMsg(ResultCode.USER_ID_EXIST);
                return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }


    private String createNewToken(String userId) {
        Date now = new Date(System.currentTimeMillis());
        Date expiration = new Date(now.getTime() + 864000000);
        return Jwts
                .builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setIssuer("Devin")
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, "winxinshare1.0")
                .compact();
    }

    public ResponseEntity search(User user) {
        logger.info("UserService.search: user={}", user.toString());
        Result<User> result = new Result<>();
        if(user.getUserId() == null || "".equals(user.getUserId())) {
            logger.info("UserService.delete: invalidParams={}", user.toString());
            result.setCodeAndMsg(ResultCode.USER_ID_EXIST);
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        } else {
            User userFromDb = userRepository.findUserByUserId(user.getUserId());
            if(userFromDb == null) {
                logger.info("UserService.search: userNotFound={}", user.toString());
                result.setCodeAndMsg(ResultCode.USER_NOT_EXIST);
                return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
            } else {
                userFromDb.setUserPwd(null);
                userFromDb.setUserId(null);
                userFromDb.setLastLoginTime(null);
                result.setData(userFromDb);
                result.setCodeAndMsg(ResultCode.SUCCESS);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }
    }
}
