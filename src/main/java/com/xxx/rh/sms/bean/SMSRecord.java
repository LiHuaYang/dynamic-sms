package com.xxx.rh.sms.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信发送记录
 * @author lihy
 * @version 1.0  2018/7/3
 */
public class SMSRecord implements Serializable {

    private static final long serialVersionUID = 5065447801734585925L;
    /** VARCHAR2(32)	N	GUID */
    public  String urid;
    /** VARCHAR2(15)	N	手机号 */
    public  String phoneNum;
    /** CHAR(1)	N	短信渠道。1：阿里，2：百度，3：腾讯 */
    public  String channel;
    /** Date	N	发送日期 */
    public Date sendTime;

    public String getUrid() {
        return urid;
    }

    public void setUrid(String urid) {
        this.urid = urid;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "SMSRecord{" +
                "urid='" + urid + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", channel='" + channel + '\'' +
                ", sendTime=" + sendTime +
                '}';
    }
}
