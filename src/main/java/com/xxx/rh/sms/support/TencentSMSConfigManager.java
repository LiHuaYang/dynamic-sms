package com.xxx.rh.sms.support;

import com.xxx.rh.sms.bean.SMSRule;
import com.xxx.rh.sms.bean.TencentSMSConfig;

import java.util.Map;
import java.util.Set;

/**
 * 腾讯短信渠道配置管理
 * @author lihy
 * @version 1.0  2018/7/4
 */
public class TencentSMSConfigManager implements SMSConfigManager {

    private TencentSMSConfig config = SMSXMLConfigLoader.getTencentSMSConfig();

    @Override
    public boolean isEnable() {
        return config.isEnable();
    }

    @Override
    public Map<String, String> getTemplateMapping() {
        return config.getTemplateMapping();
    }

    @Override
    public Set<SMSRule> getRules() {
        return config.getRules();
    }

    @Override
    public String getAppId() {
        return config.getSdkAppId();
    }

    @Override
    public String getAppKey() {
        return config.getAppKey();
    }

    @Override
    public Map<String, String> getSignMapping() {
        return config.getSignMapping();
    }
}
