package com.xxx.rh.sms.bean;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 阿里短信配置
 * @author lihy
 * @version 1.0  2018/7/1
 */
public class AliSMSConfig implements Serializable {

    // 是否启用
    private boolean enable;
    // 优先级
    private int priority;
    // 阿里云颁发给用户的访问服务所用的密钥ID
    private String accessKeyID;
    // 用户加密签名字符串和服务器端
    private String accessKeySecret;
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

    public String getAccessKeyID() {
        return accessKeyID;
    }

    public void setAccessKeyID(String accessKeyID) {
        this.accessKeyID = accessKeyID;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
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
