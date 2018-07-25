package com.xxx.rh.sms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.xxx.rh.sms.bean.SMSRequestDto;
import com.xxx.rh.sms.common.Constants;
import com.xxx.rh.sms.common.Globals;
import com.xxx.rh.sms.common.IOUtils;
import com.xxx.rh.sms.support.SMSConfigManager;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 阿里云短信服务实现类
 *
 * 开发文档：
 * https://help.aliyun.com/product/44282.html
 *
 * @author lihy
 * @version 1.0  2018/7/2
 */
public class AliSMSServiceImpl extends AbstractSMSServiceImpl {

    private SMSConfigManager config = (SMSConfigManager) Globals.getBean("aliSMSConfigManager");

    @Override
    public String send(Map<String, Object> param) {
        logger.info("阿里短信发送服务开始");
        IAcsClient acsClient = getClient(config.getAppId(), config.getAppKey());
        if (acsClient == null) {
            logger.error("生成短信发送对象失败");
            return null;
        }

        SMSRequestDto dto = (SMSRequestDto)param.get("dto");
        SendSmsRequest request = getSendRequest(dto);
        if (request == null) {
            logger.error("生成短信请求对象失败");
            return null;
        }

        SendSmsResponse response = null;
        try {
            response = acsClient.getAcsResponse(request);
            logger.info(String.format("短信发送返回 requestId[%s], bizId[%s], code[%s], message[%s]",
                    response.getRequestId(), response.getBizId(),response.getCode(),response.getMessage()));
        } catch (Exception e) {
            logger.error("调用阿里 sdk 发送短信时发生异常", e);
            return IOUtils.getResp(false, "发送失败");
        }

        if (response.getCode() != null && response.getCode().equals("OK")) {
            return IOUtils.getResp(true, "发送成功");
        }
        return IOUtils.getResp(false, String.format("发送失败:%s", response.getMessage()));
    }

    @Override
    public boolean enable(String phoneNum) {
        return super.enable(phoneNum, config);
    }

    @Override
    public String getChannel() {
        return Constants.CHANNEL_ALI;
    }

    private IAcsClient getClient(String appId, String key) {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", appId, key);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
        } catch (Exception e) {
            logger.error("创建 IAcsClient 时异常", e);
            return null;
        }
        return new DefaultAcsClient(profile);
    }

    private SendSmsRequest getSendRequest(SMSRequestDto dto) {
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
        request.setPhoneNumbers(dto.getPhone() == null ? "" : dto.getPhone());
        //必填:短信签名-可在短信控制台中找到
        String signName = config.getSignMapping().get(dto.getSign());
        request.setSignName(StringUtils.isBlank(signName) ? "融易保" : signName);
        //必填:短信模板-可在短信控制台中找到
        String templateCode = config.getTemplateMapping().get(dto.getType());
        request.setTemplateCode(templateCode == null ? "" : templateCode);

        // 验证码正文
        JSONObject json = new JSONObject();
        json.put("code", dto.getContent()== null ? "" : dto.getContent());

        String type = dto.getType();
        if (Constants.SMSTYPE_BIND_CARD.equals(type)
                || Constants.SMSTYPE_UNBIND_CARD.equals(type)
                || Constants.SMSTYPE_PAY.equals(type)) {
            String four = dto.getParams()[0];
            json.put("account", four == null ? "" : four);
            String name = dto.getParams()[1];
            json.put("bank", name == null ? "" : name);
        }
        request.setTemplateParam(json.toJSONString());
        return request;
    }
}
