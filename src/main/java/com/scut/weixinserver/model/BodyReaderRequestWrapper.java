package com.scut.weixinserver.model;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scut.weixinserver.utils.AES;

import java.io.*;

public class BodyReaderRequestWrapper extends HttpServletRequestWrapper {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private  String body;

    /**
     *
     * @param request
     */
    public BodyReaderRequestWrapper(HttpServletRequest request) throws IOException{
        super(request);
        StringBuilder sb = new StringBuilder();
        InputStream ins = request.getInputStream();
        BufferedReader isr = null;
        try{
            if(ins != null){
                isr = new BufferedReader(new InputStreamReader(ins));
                char[] charBuffer = new char[128];
                int readCount = 0;
                while((readCount = isr.read(charBuffer)) != -1){
                    sb.append(charBuffer,0,readCount);
                }
            }else{
                sb.append("");
            }
        }catch (IOException e){
            throw e;
        }finally {
            if(isr != null) {
                isr.close();
            }
        }

        sb.toString();
        body = sb.toString();


    }
     public boolean tryDecrypt(HttpServletRequest request){
    	 String AESkey=(String) request.getSession().getAttribute("AESkey");
         if(AESkey==null)
        	 return false;
         else{
        	 logger.info("解密前:"+body);
        	 body=AES.decrypt(body, AESkey);
        	 logger.info("解密后："+body);
             return true;
         }
     }
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayIns = new ByteArrayInputStream(body.getBytes());
        ServletInputStream servletIns = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayIns.read();
            }
        };
        return  servletIns;
    }
}