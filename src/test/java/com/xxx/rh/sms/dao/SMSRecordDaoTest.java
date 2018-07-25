package com.xxx.rh.sms.dao;

import com.xxx.rh.sms.bean.SMSRecord;
import com.xxx.rh.sms.common.Constants;
import com.xxx.rh.sms.dao.impl.SMSRecordDaoImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lihy
 * @version 1.0  2018/7/4
 */
public class SMSRecordDaoTest {

    SMSRecordDao dao = null;

    @Before
    public void init() {
        dao = new SMSRecordDaoImpl();
    }

    @Test
    public void getRecords() throws Exception {
        // 2018/7/3 11:33:41
        // 2018/7/3 12:51:24
        Date d1 = Constants.s1.parse("2018-07-03 10:03:10");
        Date d2 = Constants.s1.parse("2018-07-04 13:03:10");

        Date d3 = Constants.s1.parse("2018-07-04 14:03:10");
        Date d4 = Constants.s1.parse("2018-07-04 14:03:10");

        Date d5 = Constants.s1.parse("2018-07-03 11:33:41");
        Date d6 = Constants.s1.parse("2018-07-03 12:51:24");

        // 有记录
        List<SMSRecord> s1 = dao.getRecords("15728046336", "1", d1, d2);
//        for (SMSRecord item : s1) {
//            System.out.println(Constants.s1.format(item.getSendTime()));
//        }
        // 无记录
        List<SMSRecord> s2 = dao.getRecords("15728046336", "1", d3, d4);
//        for (SMSRecord item : s2) {
//            System.out.println(Constants.s1.format(item.getSendTime()));
//        }
        // 边界测试
        List<SMSRecord> s3 = dao.getRecords("15728046336", "1", d5, d6);
        for (SMSRecord item : s3) {
            System.out.println(Constants.s1.format(item.getSendTime()));
        }
    }

    @Test
    public void getSubList() throws Exception {
        List<SMSRecord> smsRecords = new ArrayList<>();
        SMSRecord r1 = new SMSRecord();
        r1.setSendTime(new Date());
        SMSRecord r2 = new SMSRecord();
        r2.setSendTime(Constants.s1.parse("2018-07-04 11:02:10"));
        SMSRecord r3 = new SMSRecord();
        r3.setSendTime(Constants.s1.parse("2018-07-04 11:02:10"));
        SMSRecord r4 = new SMSRecord();
        r4.setSendTime(Constants.s1.parse("2018-07-04 19:05:10"));
        smsRecords.add(r1);
        smsRecords.add(r2);
        smsRecords.add(r3);
        smsRecords.add(r4);

        // 包含
        Date d1 = Constants.s1.parse("2018-07-03 14:03:10");
        Date d2 = Constants.s1.parse("2018-07-06 15:02:10");
        List<SMSRecord> res = dao.getSubList(smsRecords, d1, d2);
        for (SMSRecord item : res) {
            System.out.println(Constants.s1.format(item.getSendTime()));
        }

        // 不包含
        d1 = Constants.s1.parse("2018-07-05 15:01:10");
        d2 = Constants.s1.parse("2018-07-05 15:02:10");
        res = dao.getSubList(smsRecords, d1, d2);
        for (SMSRecord item : res) {
            // System.out.println(Constants.s1.format(item.getSendTime()));
        }
    }

}