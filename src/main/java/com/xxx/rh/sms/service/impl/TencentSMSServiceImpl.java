package com.xxx.rh.sms.service.impl;

import com.xxx.rh.sms.bean.SMSRequestDto;
import com.xxx.rh.sms.common.Constants;
import com.xxx.rh.sms.common.Globals;
import com.xxx.rh.sms.common.IOUtils;
import com.xxx.rh.sms.support.SMSConfigManager;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 腾讯短信服务实现类
 *
 * 开发文档：
 * https://cloud.tencent.com/document/product/382
 *
 * @author lihy
 * @version 1.0  2018/7/2
 */
public class TencentSMSServiceImpl extends AbstractSMSServiceImpl {

    private static Integer count = 0;

    private SMSConfigManager config = (SMSConfigManager) Globals.getBean("tencentSMSConfigManager");

    @Override
    public String send(Map<String, Object> param) {
        logger.info("腾讯短信发送服务开始");

        SMSRequestDto dto = (SMSRequestDto)param.get("dto");
        String phoneNumber = dto.getPhone();
        String content = dto.getContent();
        String smsType = dto.getType();
        String sign = dto.getSign();

        int appid = Integer.valueOf(config.getAppId());
        String appkey = config.getAppKey();

        String temp = config.getTemplateMapping().get(smsType);
        int templateId = Integer.valueOf(temp);

        String signName = config.getSignMapping().get(sign);
        String smsSign = StringUtils.isBlank(signName) ? "融易保" : signName;

        try {
            String[] params = {content};
            if (Constants.SMSTYPE_BIND_CARD.equals(smsType) || Constants.SMSTYPE_UNBIND_CARD.equals(smsType)
                    || Constants.SMSTYPE_PAY.equals(smsType)) {
                String four = dto.getParams()[0];
                String name = dto.getParams()[1];
                params = new String[]{content, four, name};
            }
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            // 签名参数未提供或者为空时，会使用默认签名发送短信
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber, templateId, params, smsSign, "", "");
            logger.info(String.format("短信渠道返回result[%s] errmsg[%s] ext[%s] fee[%s] sid[%s]",
                    result.result, result.errMsg, result.ext, result.fee, result.sid));
            if (result.result != 0) {
                return IOUtils.getResp(false, String.format("发送失败:%s", result.errMsg));
            }
        } catch (Exception e) {
            logger.error("调用腾讯短信服务异常", e);
            return IOUtils.getResp(false, "发送失败");
        }
        return IOUtils.getResp(true, "发送成功");
    }

    @Override
    public boolean enable(String phoneNum) {
        return super.enable(phoneNum, config);
    }

    @Override
    public String getChannel() {
        return Constants.CHANNEL_TENCENT;
    }
}