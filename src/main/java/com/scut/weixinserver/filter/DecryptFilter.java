package com.scut.weixinserver.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.scut.weixinserver.model.BodyReaderRequestWrapper;
import com.scut.weixinserver.model.EncryptBean;
import com.scut.weixinserver.model.ResponseBodyWrapper;
import com.scut.weixinserver.utils.AES;

/**
 *  
 * 
 * @author hts
 * @version date：2018年4月11日 下午7:14:04 
 * 
 */
@Order(1)
@WebFilter(filterName = "DecryptFilter", urlPatterns = "/*")
public class DecryptFilter implements Filter {
	private static final Set<String> ALLOWED_PATHS = Collections
			.unmodifiableSet(new HashSet<>(Arrays.asList("/key/keyExchange")));
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		logger.info("——————————————————解密filter开始————————————————");

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		if (request.getCookies() != null)
			logger.info(request.getCookies().toString());
		// 排除不需要解密的url
		String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
		boolean allowedPath = ALLOWED_PATHS.contains(path);
		//如果是不需要加密解密的地址，正常进入controller
		if (allowedPath) {
			logger.info("——————————————————解密filter结束————————————————");
			filterChain.doFilter(request, response);
			return;
		}
		//如果是multipart/form-data类型，不进行加密解密
		if(request.getContentType() != null && request.getContentType().contains("multipart/form-data")){
			logger.info("multipart/form-data类型，不需要加解密！！");
			logger.info("——————————————————解密filter结束————————————————");
			filterChain.doFilter(request, response);
			return;
		}
		// application/json类型进行解密操作
		BodyReaderRequestWrapper requestWrapper = new BodyReaderRequestWrapper(request);
		ResponseBodyWrapper wrapResponse = new ResponseBodyWrapper((HttpServletResponse) response);
		//如果存在SESSION中存在AES key，流程正常进行
		if (requestWrapper.tryDecrypt(requestWrapper)) {
			filterChain.doFilter(requestWrapper, wrapResponse);
			//如果不是200，程序错误，不需加密
			if(wrapResponse.getStatus()!=200){
				response.setStatus(wrapResponse.getStatus());
				writeResponse(response,new String(wrapResponse.getResponseData(), "utf-8"));
			}
			// 加密操作
			byte[] data = wrapResponse.getResponseData();
			logger.info("原始返回数据： " + new String(data, "utf-8"));
			String AESkey = (String) request.getSession().getAttribute("AESkey");
			// 加密返回报文
			logger.info("——————————————————加密filter开始————————————————");
			String responseBodyMw = AES.encrypt(new String(data, "utf-8"), AESkey);
			EncryptBean encryptBean = new EncryptBean();
			encryptBean.setSuccess(true);
			encryptBean.setEncyptData(responseBodyMw);
			String finalMessage = new Gson().toJson(encryptBean);
			logger.info("加密返回数据： " + finalMessage);
			//将假response的status复制到真response
			response.setStatus(wrapResponse.getStatus());
			writeResponse(response, finalMessage);
			logger.info("——————————————————加密filter结束————————————————");
		} else {
			logger.warn("注意：获取AES KEY失败！！！");
			PrintWriter writer = null;
			OutputStreamWriter osw = null;
			osw = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
			writer = new PrintWriter(osw, true);
			EncryptBean encryptBean = new EncryptBean();
			encryptBean.setSuccess(false);
			String jsonStr = new Gson().toJson(encryptBean);
			writer.write(jsonStr);
			writer.flush();
			writer.close();
			osw.close();
			logger.info("——————————————————解密filter结束————————————————");
		}

	}

	private void writeResponse(ServletResponse response, String responseString) throws IOException {
		PrintWriter out = response.getWriter();
		out.print(responseString);
		out.flush();
		out.close();
	}

	@Override
	public void destroy() {

	}
}
