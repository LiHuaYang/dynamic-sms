package com.xxx.rh.sms.support;

import com.xxx.rh.sms.bean.SystemConfig;
import com.xxx.rh.sms.bean.TencentSMSConfig;
import com.xxx.rh.sms.common.Constants;
import com.xxx.rh.sms.bean.AliSMSConfig;
import com.xxx.rh.sms.bean.SMSRule;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

/**
 * XML 格式的短信渠道配置加载器
 * @author lihy
 * @version 1.0  2018/7/1
 */
public class SMSXMLConfigLoader {

    private static Logger logger = Logger.getLogger(SMSXMLConfigLoader.class);

    private static AliSMSConfig aliSMSConfig;

    private static TencentSMSConfig tencentSMSConfig;

    private static SystemConfig systemConfig;

    public static AliSMSConfig getAliSMSConfig() {
        return aliSMSConfig;
    }

    public static TencentSMSConfig getTencentSMSConfig() {
        return tencentSMSConfig;
    }

    public static SystemConfig getSystemConfig() {
        return systemConfig;
    }

    public static void refresh() {

    }

    static {
        // 加载短信配置文件
        Document smsConfigXml = load("smsConfig.xml");
        if (smsConfigXml == null) {
            System.exit(0);
        }
        // 校验配置文件，TODO 暂时只校验了阿里
        if (!checkAliConfigXmllegality(smsConfigXml)) {
            logger.error("校验配置文件异常");
            System.exit(0);
        }
        aliSMSConfig = praseAliConfigObj(smsConfigXml);
        tencentSMSConfig = praseTencentConfigObj(smsConfigXml);

        // 加载系统配置文件
        Document sysConfigXml = load("sysConfig.xml");
        if (sysConfigXml == null) {
            System.exit(0);
        }
        systemConfig = praseSysConfigObj(sysConfigXml);
    }

    private static Document load(String resourceClassPath) {
        try {
            Resource resource = new ClassPathResource(resourceClassPath);
            File configFile = resource.getFile();
            logger.info("开始加载配置文件：" + resource.getFile().getAbsolutePath());
            String configStr = FileUtils.readFileToString(configFile, Charset.forName(Constants.charset));
            return DocumentHelper.parseText(configStr);
        } catch (Exception e) {
            logger.error("加载配置文件出现异常，无法启动系统", e);
            return null;
        }
    }

    private static boolean checkAliConfigXmllegality(Document configXml) {
        logger.info("检查配置文件正确性");
        Node enableNode = configXml.selectSingleNode("/Root/Aliyun/Enable");
        if (enableNode == null) {
            logger.error("/Root/Aliyun/Enable 节点为空");
            return false;
        }
        Node priorityNode = configXml.selectSingleNode("/Root/Aliyun/Priority");
        if (priorityNode == null) {
            logger.error("/Root/Aliyun/Priority 节点为空");
            return false;
        }
        Node accesskeyidnode = configXml.selectSingleNode("/Root/Aliyun/AccessKeyID");
        if (accesskeyidnode == null) {
            logger.error("/Root/Aliyun/AccessKeyID 节点为空");
            return false;
        }
        Node accesskeysecretnode = configXml.selectSingleNode("/Root/Aliyun/AccessKeySecret");
        if (accesskeysecretnode == null) {
            logger.error("/Root/Aliyun/AccessKeySecret 节点为空");
            return false;
        }
        Node signnamenode = configXml.selectSingleNode("/Root/Aliyun/SignName");
        if (signnamenode == null) {
            logger.error("/Root/Aliyun/SignName 节点为空");
            return false;
        }
        Node templatenode = configXml.selectSingleNode("/Root/Aliyun/SMSTemplates");
        return templatenode != null;
    }

    private static AliSMSConfig praseAliConfigObj(Document configXml) {
        logger.info("解析成内部配置对象 AliSMSConfig");
        AliSMSConfig config = new AliSMSConfig();
        config.setEnable(Boolean.valueOf(configXml.selectSingleNode("/Root/Aliyun/Enable").getText()));
        config.setPriority(Integer.valueOf(configXml.selectSingleNode("/Root/Aliyun/Priority").getText()));
        config.setAccessKeyID(configXml.selectSingleNode("/Root/Aliyun/AccessKeyID").getText());
        config.setAccessKeySecret(configXml.selectSingleNode("/Root/Aliyun/AccessKeySecret").getText());

        Map<String, String> signMap = new HashMap<>();
        List<Node> smsSignNodes = configXml.selectNodes("/Root/Aliyun/SMSSigns/SMSSign");
        for (Node item : smsSignNodes) {
            String code =item.selectSingleNode("FGSignCode").getText();
            String name = item.selectSingleNode("Name").getText();
            signMap.put(code, name);
        }
        config.setSignMapping(signMap);

        Map<String, String> templatMap = new HashMap<>();
        List<Node> nodes = configXml.selectNodes("/Root/Aliyun/SMSTemplates/SMSTemplate");
        for (Node item : nodes) {
            String type =item.selectSingleNode("FGSMSType").getText();
            String aliId = item.selectSingleNode("TemplateID").getText();
            templatMap.put(type, aliId);
        }
        config.setTemplateMapping(templatMap);

        /** 按照时间跨度进行排序 */
        Set<SMSRule> sets = new TreeSet<>();
        List<Node> nodes2 = configXml.selectNodes("/Root/Aliyun/Rules/Rult");
        for (Node item : nodes2) {
            SMSRule smsRule = new SMSRule();
            Long time = Long.valueOf(item.selectSingleNode("Time").getText());
            Integer count = Integer.valueOf(item.selectSingleNode("MaxCount").getText());
            smsRule.setTime(time);
            smsRule.setCount(count);
            sets.add(smsRule);
        }
        config.setRules(sets);
        return config;
    }

    private static TencentSMSConfig praseTencentConfigObj(Document configXml) {
        logger.info("解析成内部配置对象 TencentSMSConfig");
        TencentSMSConfig config = new TencentSMSConfig();
        config.setEnable(Boolean.valueOf(configXml.selectSingleNode("/Root/Tencent/Enable").getText()));
        config.setPriority(Integer.valueOf(configXml.selectSingleNode("/Root/Tencent/Priority").getText()));
        config.setSdkAppId(configXml.selectSingleNode("/Root/Tencent/SDKAppId").getText());
        config.setAppKey(configXml.selectSingleNode("/Root/Tencent/AppKey").getText());

        Map<String, String> signMap = new HashMap<>();
        List<Node> smsSignNodes = configXml.selectNodes("/Root/Tencent/SMSSigns/SMSSign");
        for (Node item : smsSignNodes) {
            String code =item.selectSingleNode("FGSignCode").getText();
            String name = item.selectSingleNode("Name").getText();
            signMap.put(code, name);
        }
        config.setSignMapping(signMap);

        Map<String, String> map = new HashMap<>();
        List<Node> nodes = configXml.selectNodes("/Root/Tencent/SMSTemplates/SMSTemplate");
        for (Node item : nodes) {
            String type =item.selectSingleNode("FGSMSType").getText();
            String aliId = item.selectSingleNode("TemplateID").getText();
            map.put(type, aliId);
        }
        config.setTemplateMapping(map);

        Set<SMSRule> sets = new TreeSet<>();
        List<Node> nodes2 = configXml.selectNodes("/Root/Tencent/Rules/Rult");
        for (Node item : nodes2) {
            SMSRule smsRule = new SMSRule();
            Long time = Long.valueOf(item.selectSingleNode("Time").getText());
            Integer count = Integer.valueOf(item.selectSingleNode("MaxCount").getText());
            smsRule.setTime(time);
            smsRule.setCount(count);
            sets.add(smsRule);
        }
        config.setRules(sets);
        return config;
    }

    private static SystemConfig praseSysConfigObj(Document configXml) {
        logger.info("解析成内部配置对象 TencentSMSConfig");
        SystemConfig config = new SystemConfig();
        return config;
    }
}
