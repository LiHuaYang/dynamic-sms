package com.xxx.rh.sms.service;

import java.util.Map;

/**
 * 短信服务接口
 * @author lihy
 * @version 1.0  2018/7/2
 */
public interface SMSService {

    /**
     * 发送短信接口，返回响应 json
     * @author lihy  v1.0   2018/7/2
     */
    String send(Map<String, Object> param);

    /**
     * 服务是否可用
     * @author lihy  v1.0   2018/7/3
     */
    boolean enable(String phoneNum);

    /**
     * 返回渠道编号
     * @author lihy  v1.0   2018/7/10
     */
    String getChannel();
}
