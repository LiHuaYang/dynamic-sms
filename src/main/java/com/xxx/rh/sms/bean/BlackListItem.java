package com.xxx.rh.sms.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 黑名单单条项
 * @author lihy
 * @version 1.0  2018/7/4
 */
public class BlackListItem implements Serializable {

    private String phoneNum;

    private Date expiredTime;

    public BlackListItem() {

    }

    public BlackListItem(String phoneNum, Date expiredTime) {
        this.phoneNum = phoneNum;
        this.expiredTime = expiredTime;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }
}
