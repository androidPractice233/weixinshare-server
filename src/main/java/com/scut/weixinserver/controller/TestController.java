package com.scut.weixinserver.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.scut.weixinserver.model.APIBodyData;
import com.scut.weixinserver.model.PageData;
import com.scut.weixinserver.model.ResultBean;

/** 
* @author hts
* @version date：2018年4月10日 下午11:05:56 
* 
*/
@Controller
public class TestController extends BaseController {
    @RequestMapping("/test") 
    @ResponseBody
    public ResultBean test(APIBodyData data) throws FileNotFoundException, Exception{  
      logger.info("test测试："+data.toString());
      ResultBean result=new ResultBean();
      result.setData("cao ni ma");
      return result;
       
    }  
    @RequestMapping("/MultipartTest") 
    @ResponseBody
    public ResultBean multipartTest(@RequestParam(value = "fileList", required = false) List<MultipartFile> files,String test ) throws FileNotFoundException, Exception{  
      logger.info("test测试："+test);
      logger.info("test测试："+test);
      ResultBean result=new ResultBean();
      result.setData("cao ni ma");
      return result;
       
    }  
}
