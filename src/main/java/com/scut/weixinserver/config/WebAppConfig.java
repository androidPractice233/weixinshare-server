package com.scut.weixinserver.config;

import com.scut.weixinserver.interceptor.JwtTokenInterceptor;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.MultipartConfigElement;

@Configuration
public class WebAppConfig extends WebMvcConfigurationSupport {


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大大小
        factory.setMaxFileSize("20MB");
        //总数据大小
        factory.setMaxRequestSize("200MB");
        return factory.createMultipartConfig();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截/api/下的两级接口，注册除外
        registry.addInterceptor(new JwtTokenInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/user/register", "/piccontent/**", "/portrait/**");
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/portrait/**").addResourceLocations("file:/home/devin/weixinshare/portrait/");
        registry.addResourceHandler("/piccontent/**").addResourceLocations("file:/home/devin/weixinshare/piccontent/");
        super.addResourceHandlers(registry);

    }
}
