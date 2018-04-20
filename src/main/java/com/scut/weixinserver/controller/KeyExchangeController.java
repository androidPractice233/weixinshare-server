package com.scut.weixinserver.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.interfaces.RSAPrivateKey;

import javax.servlet.http.HttpSession;

import com.scut.weixinserver.model.Result;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.scut.weixinserver.model.APIBodyData;
import com.scut.weixinserver.model.PageData;
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
	@RequestMapping("/key/keyExchange")
	@ResponseBody
	public Result keyExchange(@RequestBody String data, HttpSession session)
			throws FileNotFoundException, Exception {
		data = new Gson().fromJson(data, String.class);
		logger.info("加密后的AESkey:" + data);
		String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
		String osAppropriatePath = System.getProperty("os.name").contains("indow") ? path.substring(1) : path;
		osAppropriatePath += "RSA";
		RSAPrivateKey privateKey = RSA
				.loadPrivateKey(new FileInputStream(osAppropriatePath += "/pkcs8_rsa_private_key.pem"));
		String AESkey = RSA.decryptByPrivateKey(data, privateKey);
		logger.info("AESkey:" + AESkey);
		// logger.info("AESkey:"+AESkey);
		// System.out.println(AESkey);
		session.setAttribute("AESkey", AESkey);
		return new Result();
	}

}
