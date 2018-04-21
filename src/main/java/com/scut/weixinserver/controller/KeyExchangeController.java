package com.scut.weixinserver.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.interfaces.RSAPrivateKey;

import javax.servlet.http.HttpSession;

import com.scut.weixinserver.model.Result;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.scut.weixinserver.model.APIBodyData;

import com.scut.weixinserver.utils.RSA;

/**
 *  
 * 
 * @author hts
 * @version date：2018年4月11日 下午2:19:17 
 * 
 */
@Controller
public class KeyExchangeController extends BaseController {
	@Value(value = "classpath:RSA/pkcs8_rsa_private_key.pem")
	private Resource RSAprivateKey;
	@RequestMapping("/key/keyExchange")
	@ResponseBody
	public Result keyExchange(@RequestBody String data, HttpSession session)
			throws FileNotFoundException, Exception {
		data = new Gson().fromJson(data, String.class);
		logger.info("加密后的AESkey:" + data);
		RSAPrivateKey privateKey = RSA
				.loadPrivateKey(RSAprivateKey.getInputStream());
		String AESkey = RSA.decryptByPrivateKey(data, privateKey);
		logger.info("AESkey:" + AESkey);
		// logger.info("AESkey:"+AESkey);
		// System.out.println(AESkey);
		session.setAttribute("AESkey", AESkey);
		return new Result();
	}

}
