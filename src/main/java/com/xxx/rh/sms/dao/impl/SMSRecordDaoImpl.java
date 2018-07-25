package com.xxx.rh.sms.dao.impl;

import com.xxx.rh.sms.bean.SMSRecord;
import com.xxx.rh.sms.common.Constants;
import com.xxx.rh.sms.common.JdbcUtils;
import com.xxx.rh.sms.dao.SMSRecordDao;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * @author lihy
 * @version 1.0  2018/7/3
 */
public class SMSRecordDaoImpl implements SMSRecordDao {

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public List<SMSRecord> getRecords(String phoneNum, String channel, Date startTime, Date endTime) {
        // FIXME 是不是有更好的方法转换 Date 类型，换成 TimeStemp 试试
        String sql = "select to_char(t.sendtime, 'yyyy-mm-dd hh24:mi:ss') stime, t.* from smsrecord t where 1=1 and phoneNum=? and channel=? "
                + " and t.sendtime>=to_date(?,'yyyy-mm-dd hh24:mi:ss') "
                + " and t.sendtime<=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
        List<SMSRecord> res = null;
        try {
            QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
            String startTimeStr = Constants.s1.format(startTime);
            String endTimeStr = Constants.s1.format(endTime);
            List<Map<String, Object>> list = qr.query(sql, new MapListHandler(), new Object[]{phoneNum, channel, startTimeStr, endTimeStr});
            res = new ArrayList<>(list.size());
            for (Map<String, Object> item : list) {
                SMSRecord record = new SMSRecord();
                record.setUrid((String) item.get("urid".toUpperCase()));
                record.setPhoneNum((String) item.get("phoneNum".toUpperCase()));
                record.setChannel((String) item.get("channel".toUpperCase()));
                record.setSendTime(Constants.s1.parse((String)item.get("stime".toUpperCase())));
                res.add(record);
            }
        }
        catch (Exception e) {
            logger.error("数据库操作异常", e);
            return new ArrayList<>();
        }
        return res;
    }

    @Override
    public List<SMSRecord> getSubList(List<SMSRecord> source, Date start, Date end) {
        if (source == null || source.isEmpty()) {
            return new ArrayList<>(0);
        }
        List<SMSRecord> sub = new ArrayList<>();
        for (SMSRecord record : source) {
            if (record.getSendTime().compareTo(start)>=0 && record.getSendTime().compareTo(end)<=0) {
                sub.add(record);
            }
        }
        // 按照时间排序
        Collections.sort(sub, new Comparator<SMSRecord>() {
            @Override
            public int compare(SMSRecord arg1, SMSRecord arg2) {
                return arg1.getSendTime().compareTo(arg2.getSendTime());
            }
        });
        return sub;
    }

    @Override
    public int save(String phone, String channel) {
        try {
            QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
            String sql = "insert into SMSRecord(PhoneNum, Channel) values(?, ?)";
            return qr.update(sql, new Object[]{phone, channel});
        } catch (Exception e) {
            logger.error("数据库操作异常", e);
            return 0;
        }
    }
}
