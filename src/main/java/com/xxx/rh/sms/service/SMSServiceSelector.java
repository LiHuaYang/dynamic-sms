package com.xxx.rh.sms.service;

import com.alibaba.fastjson.JSONObject;
import com.xxx.rh.sms.bean.SMSRequestDto;
import com.xxx.rh.sms.common.Globals;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Random;

/**
 * 短信服务分发器
 * @author lihy
 * @version 1.0  2018/7/2
 */
public class SMSServiceSelector {

    private Logger logger = Logger.getLogger(this.getClass());

    private SMSServiceArrayManager smsServiceManager = (SMSServiceArrayManager)Globals.getBean("smsServiceArrayManager");

    private Random random = new Random();

    private static volatile Integer pos = 0;

    /**
     * 获取可用服务，如果服务不可用则自动切换到下个可用服务
     * 当全部服务不可用时，返回 null
     * @author lihy  v1.0   2018/7/2
     */
    public SMSService getEnableService(Map<String, Object> context) {
        SMSRequestDto dto = (SMSRequestDto) context.get("dto");
        //
        SMSService target = getNextService();

        if ((Integer)context.get("svrCount") == 0) {
            logger.error("递归结束，当前所注册服务中无可用服务，返回为 null");
            return null;
        }
        if (target != null && !target.enable(dto.getPhone())) {
            context.put("svrCount", (Integer) context.get("svrCount") - 1);
            return getEnableService(context);
        }
        return target;
    }

    /**
     * 依次取下一个服务（轮询）
     * @author lihy  v1.0   2018/7/2
     */
    private SMSService getNextService() {
        SMSService service = null;
        synchronized (pos)  {
            if (pos >= smsServiceManager.size()) {
                pos = 0;
            }
            service = smsServiceManager.get(pos);
            pos ++;
        }
        return service;
    }

    /**
     * 随机取下一个服务
     * @author lihy  v1.0   2018/7/2
     */
    private SMSService getRandomService() {
        return smsServiceManager.get(random.nextInt(smsServiceManager.size()));
    }
}
