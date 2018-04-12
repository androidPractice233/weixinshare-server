package com.scut.weixinserver.model;

/**
 * 
* @author hts
* @version date：2018年4月11日 下午7:37:46 
*
 */

public class EncryptBean {
    private boolean success;
    private String encyptData;
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getEncyptData() {
        return encyptData;
    }

    public void setEncyptData(String encyptData) {
        this.encyptData = encyptData;
    }
}
