package com.xxx.rh.sms.bean;

import java.io.Serializable;

/**
 * 短信规则
 * @author lihy
 * @version 1.0  2018/7/4
 */
public class SMSRule implements Serializable, Comparable<SMSRule> {
    /** 时间间隔 */
    private Long time;
    /** 限定次数 */
    private Integer count;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public int compareTo(SMSRule arg) {
        if (this.time > arg.getTime()) {
            return 1;
        }
        else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SMSRule smsRule = (SMSRule) o;
        if (time != null ? !time.equals(smsRule.time) : smsRule.time != null) {
            return false;
        }
        return count != null ? count.equals(smsRule.count) : smsRule.count == null;
    }

    @Override
    public int hashCode() {
        int result = time != null ? time.hashCode() : 0;
        result = 31 * result + (count != null ? count.hashCode() : 0);
        return result;
    }
}
