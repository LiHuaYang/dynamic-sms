package com.xxx.rh.sms.support;

import com.xxx.rh.sms.bean.SMSRule;

import java.util.Map;
import java.util.Set;

/**
 * 短信配置管理
 * @author lihy
 * @version 1.0  2018/7/4
 */
public interface SMSConfigManager {

    /**
     *
     * @author lihy  v1.0   2018/7/4
     */
    boolean isEnable();

    /**
     *
     * @author lihy  v1.0   2018/7/4
     */
    Map<String, String> getTemplateMapping();

    /**
     * 要求按照时间跨度正序排序
     * @author lihy  v1.0   2018/7/4
     */
    Set<SMSRule> getRules();

    /**
     *
     * @author lihy  v1.0   2018/7/9
     */
    String getAppId();

    /**
     *
     * @author lihy  v1.0   2018/7/9
     */
    String getAppKey();

    /**
     *
     * @author lihy  v1.0   2018/7/10
     */
    Map<String, String> getSignMapping();

}
