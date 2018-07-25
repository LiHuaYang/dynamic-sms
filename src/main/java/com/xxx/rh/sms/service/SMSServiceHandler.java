package com.xxx.rh.sms.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xxx.rh.sms.bean.SMSRequestDto;
import com.xxx.rh.sms.common.Constants;
import com.xxx.rh.sms.common.Globals;
import com.xxx.rh.sms.common.IOUtils;
import com.xxx.rh.sms.dao.SMSRecordDao;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lihy
 * @version 1.0  2018/7/2
 */
public class SMSServiceHandler {

    private Logger logger = Logger.getLogger(this.getClass());

    private SMSServiceSelector selector = (SMSServiceSelector) Globals.getBean("smsServiceSelector");

    private SMSServiceArrayManager smsServiceManager = (SMSServiceArrayManager)Globals.getBean("smsServiceArrayManager");

    private SMSRecordDao smsRecordDao = (SMSRecordDao)Globals.getBean("smsRecordDao");

    /**
     * 处理短信业务，reqSource 为请求原文
     * @author lihy  v1.0   2018/7/11
     */
    public String handleSendReq(String reqSource) {
        logger.info("请求原文为：" + reqSource);
        Map<String, Object> reqContext = null;
        try {
            reqContext = input(reqSource);
        } catch (Exception e) {
            logger.error("请求处理异常", e);
            return IOUtils.getFailResp();
        }
        // 分发
        SMSService service = selector.getEnableService(reqContext);
        if (service == null) {
            logger.error("无可用短信服务");
            return IOUtils.getFailResp();
        }
        // 发送短信
        String result = service.send(reqContext);
        if (StringUtils.isBlank(result)) {
            logger.error("短信服务返回为空");
            return IOUtils.getFailResp();
        }
        // 保存发送记录
        saveRecord(reqContext, service.getChannel());
        return output(result);
    }

    /**
     * 保存短信发送记录
     * @author lihy  v1.0   2018/7/11
     */
    private void saveRecord(Map<String, Object> reqContext, String channel) {
        SMSRequestDto dto = (SMSRequestDto)  reqContext.get("dto");
        int res = smsRecordDao.save(dto.getPhone(), channel);
        if (res != 1) {
            logger.error(String.format("手机号[%s] 渠道[%s] 短信发送失败", dto.getPhone(), channel));
        }
    }

    /**
     * 处理输入
     * k：serviceCount；v：Integer 类型，当前提供服务总数
     * k：dto；v：SMSRequestDto 类型，请求原文
     * @author lihy  v1.0   2018/7/5
     */
    private Map<String, Object> input(String reqSource) throws IllegalArgumentException {
        // Base64 解码
        reqSource = decode(reqSource);
        if (StringUtils.isBlank(reqSource)) {
            throw new IllegalArgumentException("请求短信服务为空");
        }
        // 解析报文
        JSONObject json = null;
        try {
            json = JSON.parseObject(reqSource);
        } catch (Exception e) {
            throw new IllegalArgumentException("JSON 字符串解析异常", e);
        }
        // 参数校验
        if (!json.containsKey("PhoneNum") || StringUtils.isBlank(json.getString("PhoneNum"))) {
            throw new IllegalArgumentException("PhoneNum 参数必填，且不能为空");
        }
        if (!json.containsKey("Content") || StringUtils.isBlank(json.getString("Content"))) {
            throw new IllegalArgumentException("Content 参数必填，且不能为空");
        }
        if (!json.containsKey("SMSType") || StringUtils.isBlank(json.getString("SMSType"))) {
            throw new IllegalArgumentException("SMSType 参数必填，且不能为空");
        }
        String type = json.getString("SMSType");
        if (Constants.SMSTYPE_BIND_CARD.equals(type) || Constants.SMSTYPE_UNBIND_CARD.equals(type)
                || Constants.SMSTYPE_PAY.equals(type)) {
            if (!json.containsKey("Parameters") || StringUtils.isBlank(json.getString("Parameters"))) {
                throw new IllegalArgumentException("SMSType 为 2,3,4 时，Parameters 不可为空");
            }
        }
        // 转换成 dto 对象
        SMSRequestDto dto = new SMSRequestDto();
        dto.setPhone(json.getString("PhoneNum"));
        dto.setContent(json.getString("Content"));
        dto.setType(json.getString("SMSType"));
        JSONArray array = json.getJSONArray("Parameters");
        dto.setParams(array == null ? new String[]{} : array.toArray(new String[]{}));
        // 组装上下文环境
        Map<String, Object> reqContext = new HashMap<>();
        reqContext.put("dto", dto);
        reqContext.put("svrCount", smsServiceManager.size());
        return reqContext;
    }

    /**
     * 处理输出
     * @author lihy  v1.0   2018/7/11
     */
    private String output(String result) {
        return StringUtils.isBlank(encode(result)) ? IOUtils.getFailResp() : result;
    }

    private String decode(String source) {
        try {
            source = URLDecoder.decode(source, Constants.charset);
            byte[] temp = Base64.getDecoder().decode(source.getBytes(Constants.charset));
            return new String(temp, Constants.charset);
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    private String encode(String source) {
        try {
            String base64String = new String(Base64.getEncoder().encode(source.getBytes(Constants.charset)), Constants.charset);
            return URLEncoder.encode(base64String, Constants.charset);
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }
}
