package com.xxx.rh.sms.bean;

/**
 * 短信传输对象
 * @author lihy
 * @version 1.0  2018/7/11
 */
public class SMSRequestDto {

    private String phone;

    private String content;

    private String type;

    private String[] params;

    private String sign;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
