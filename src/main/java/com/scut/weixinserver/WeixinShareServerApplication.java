package com.scut.weixinserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class WeixinShareServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeixinShareServerApplication.class, args);
	}


}
