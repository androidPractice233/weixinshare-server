package com.scut.weixinserver.interceptor;


import com.scut.weixinserver.entity.Token;
import com.scut.weixinserver.repo.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.security.SignatureException;

@Component
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Autowired
    TokenRepository tokenRepository;
    
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    //请求之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //排除生成token的请求，且如果是options请求是core跨域预请求，设置allow对应头信息

    	String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
        if("/user/register".contentEquals(path)
                ||"/user/login".contentEquals(path)
                ||"/key/keyExchange".contentEquals(path)
                || RequestMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }

        logger.info("开始处理请求："+path);
        final String authHeader = request.getHeader("Auth-Token");
        try {
            if (authHeader == null || authHeader.trim().equals("")) {
                throw new SignatureException("not found Auth-Token");
            }
            
            final Claims claims = Jwts.parser().setSigningKey("winxinshare1.0").parseClaimsJws(authHeader).getBody();
            request.setAttribute("userId", claims.getSubject());
            Token tokenFromDb = getDAO(TokenRepository.class, request).findTokenByUserId(
                    claims.getSubject()
            );
            if(tokenFromDb == null) {
                throw new SignatureException("not found token info, please get token again");
            }
            String tokenVal = tokenFromDb.getToken();
            if (tokenVal == null || tokenVal.trim().equals("")
                ||!tokenVal.equals(authHeader)) {
                throw new SignatureException("not found token info, please get token again");
            }
        }catch (SignatureException | ExpiredJwtException e) {
        	logger.info(e.getMessage());
        	PrintWriter writer = response.getWriter();
            //返回token错误
//            response.setStatus(401);
            writer.write("{\"code\":\"401\",\n\"msg\":\"need refresh token\"}");
            writer.close();
            return false;
        }catch (final Exception e) {
            PrintWriter writer = response.getWriter();
            //返回token错误
            response.setStatus(401);
            writer.write(e.getMessage());
            writer.close();
            return false;
        }
        return true;
    }

    private <T> T getDAO(Class<T> clazz,HttpServletRequest request)
    {
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return factory.getBean(clazz);
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
