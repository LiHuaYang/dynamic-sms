package com.xxx.rh.sms.support;

import com.xxx.rh.sms.bean.AliSMSConfig;
import com.xxx.rh.sms.bean.SMSRule;

import java.util.Map;
import java.util.Set;

/**
 * 阿里短信渠道配置管理
 * @author lihy
 * @version 1.0  2018/7/4
 */
public class AliSMSConfigManager implements SMSConfigManager {

    private AliSMSConfig config = SMSXMLConfigLoader.getAliSMSConfig();

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
        return config.getAccessKeyID();
    }

    @Override
    public String getAppKey() {
        return config.getAccessKeySecret();
    }

    @Override
    public Map<String, String> getSignMapping() {
        return config.getSignMapping();
    }
}
