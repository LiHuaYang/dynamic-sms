package com.xxx.rh.sms.common;

import java.text.SimpleDateFormat;

/**
 * 常量
 * @author lihy
 * @version 1.0  2018/7/3
 */
public final class Constants {

    private Constants(){}

    public static SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String charset = "utf-8";

    public static String ROUND = "round";
    public static String RANDOM = "random";

    /** 渠道编号 1：阿里，2：百度，3：腾讯 */
    public static String CHANNEL_ALI = "1";
    public static String CHANNEL_BAIDU = "2";
    public static String CHANNEL_TENCENT = "3";

    /** 短信类型 1：登录，2：绑卡，3：解绑，4：支付 */
    public static String SMSTYPE_LOGIN = "1";
    public static String SMSTYPE_BIND_CARD = "2";
    public static String SMSTYPE_UNBIND_CARD = "3";
    public static String SMSTYPE_PAY = "4";
}
