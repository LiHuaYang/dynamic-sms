package com.xxx.rh.sms.bean;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 腾讯短信配置
 * @author lihy
 * @version 1.0  2018/7/5
 */
public class TencentSMSConfig implements Serializable {

    // 是否启用
    private boolean enable;
    // 优先级
    private int priority;
    // 阿里云颁发给用户的访问服务所用的密钥ID
    private String sdkAppId;
    // 用户加密签名字符串和服务器端
    private String appKey;
    // 短信签名模板映射
    private Map<String, String> signMapping;
    // 短信模板映射
    private Map<String, String> templateMapping;
    // 渠道规则列表需要按照时间跨度从小到大排序
    private Set<SMSRule> rules;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getSdkAppId() {
        return sdkAppId;
    }

    public void setSdkAppId(String sdkAppId) {
        this.sdkAppId = sdkAppId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Map<String, String> getSignMapping() {
        return signMapping;
    }

    public void setSignMapping(Map<String, String> signMapping) {
        this.signMapping = signMapping;
    }

    public Map<String, String> getTemplateMapping() {
        return templateMapping;
    }

    public void setTemplateMapping(Map<String, String> templateMapping) {
        this.templateMapping = templateMapping;
    }

    public Set<SMSRule> getRules() {
        return rules;
    }

    public void setRules(Set<SMSRule> rules) {
        this.rules = rules;
    }
}
