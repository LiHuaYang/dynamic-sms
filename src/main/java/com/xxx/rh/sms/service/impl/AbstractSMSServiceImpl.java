package com.xxx.rh.sms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xxx.rh.sms.bean.BlackListItem;
import com.xxx.rh.sms.bean.SMSRecord;
import com.xxx.rh.sms.bean.SMSRule;
import com.xxx.rh.sms.common.Constants;
import com.xxx.rh.sms.common.Globals;
import com.xxx.rh.sms.dao.SMSRecordDao;
import com.xxx.rh.sms.service.SMSService;
import com.xxx.rh.sms.service.SMSServiceArrayManager;
import com.xxx.rh.sms.support.BlackListManager;
import com.xxx.rh.sms.support.SMSConfigManager;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lihy
 * @version 1.0  2018/7/4
 */
public abstract class AbstractSMSServiceImpl implements SMSService {

    protected Logger logger = Logger.getLogger(this.getClass());

    private BlackListManager manager = (BlackListManager) Globals.getBean("blackListCacheManager");

    private SMSRecordDao smsRecordDao = (SMSRecordDao)Globals.getBean("smsRecordDao");

    private SMSServiceArrayManager smsServiceManager = (SMSServiceArrayManager)Globals.getBean("smsServiceArrayManager");

    protected boolean enable(String phoneNum, SMSConfigManager config) {
        logger.info(String.format("判断当前服务（%s）是否可用", this.getClass().getSimpleName()));
        if (!config.isEnable()) {
            return config.isEnable();
        }
        // 在内存中判断下次可发送短信时间是否早于当前时间
        if (manager.onBlackList(phoneNum)) {
            // 若早于，则禁止发短信
            if (!manager.isExpired(phoneNum)) {
                logger.error(String.format("手机号[%s] 超过限制,[%s] 后可再发送",
                        phoneNum, Constants.s1.format(manager.get(phoneNum).getExpiredTime())));
                return false;
            }
            // 若已到发送时间，则在列表中移除
            manager.del(phoneNum);
        }
        boolean flag = true;
        Date current = new Date();

        List<SMSRule> temp = new ArrayList<>(config.getRules());
        // 获取最后一个规则
        SMSRule lastRule = temp.get(temp.size() - 1);

        // 在当前时间基础上，倒推规则配置的时间值
        Date longAgo = DateUtils.addSeconds(current, -(lastRule.getTime().intValue()));
        // 取出时间跨度最大的规则，在数据库中获取对应发送记录，节省数据库操作次数
        List<SMSRecord> records2 = smsRecordDao.getRecords(phoneNum, Constants.CHANNEL_ALI, longAgo, current);
        if (records2.isEmpty()) {
            logger.info("最大时间段内没有短信记录，当前渠道服务可用");
            // 获取第一个规则
            SMSRule firstRule = temp.get(0);
            // 添加进禁用名单
            Date expiredTime = DateUtils.addSeconds(new Date(), firstRule.getTime().intValue());
            manager.add(new BlackListItem(phoneNum, expiredTime));
            return true;
        }
        // 计算出每个时间段以内的发送次数，并判断是否超过限定次数
        for (SMSRule rule : config.getRules()) {
            Date previous = DateUtils.addSeconds(current, -(rule.getTime().intValue()));
            List<SMSRecord> subrecords = smsRecordDao.getSubList(records2, previous, current);
            // 次数已到当前规则所限定
            if (subrecords.size() >= rule.getCount()) {
                logger.error(String.format("手机号[%s] 此渠道的短信发送次数达到配置上限，当前渠道服务不可用", phoneNum));
                // 添加进禁用名单
                Date firstTime = subrecords.get(0).getSendTime();
                Date expiredTime = DateUtils.addSeconds(firstTime, rule.getTime().intValue());
                manager.add(new BlackListItem(phoneNum, expiredTime));
                return false;
            }
        }
        return flag;
    }

    protected void register() {
        logger.info("注册服务=" + this.getClass().getSimpleName());
        smsServiceManager.register(this);
    }
}
