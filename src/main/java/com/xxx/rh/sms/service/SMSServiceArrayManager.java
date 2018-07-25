package com.xxx.rh.sms.service;

import java.util.ArrayList;
import java.util.List;

/**
 * 短信服务管理器
 * @author lihy
 * @version 1.0  2018/7/10
 */
public class SMSServiceArrayManager {

    private List<SMSService> serviceContianer = new ArrayList<>();

    /**
     * 提供注册接口
     * @author lihy  v1.0   2018/7/10
     */
    public void register(SMSService smsService) {
        serviceContianer.add(smsService);
    }

    /**
     * 根据下标获取服务，不存在返回 null
     * @author lihy  v1.0   2018/7/10
     */
    public SMSService get(int i) {
        return serviceContianer.get(i);
    }

    /**
     * 当前容器中所注册的服务数
     * @author lihy  v1.0   2018/7/10
     */
    public int size() {
        return serviceContianer.size();
    }
}
