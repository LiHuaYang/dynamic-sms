package com.xxx.rh.sms.dao;

import com.xxx.rh.sms.bean.SMSRecord;

import java.util.Date;
import java.util.List;

/**
 * @author lihy
 * @version 1.0  2018/7/3
 */
public interface SMSRecordDao {

    /**
     * 从数据库中获取指定条件的短信记录。包含边界
     * 没有符合条件的记录时，返回 size=0 的集合
     * @author lihy  v1.0   2018/7/3
     */
    List<SMSRecord> getRecords(String phoneNum, String channel, Date startTime, Date endTime);

    /**
     * 从指定的列表（source）中获取，指定时间区间（start - end）的子列表。
     * 1. 查询时，时间值包含边界
     * 2. 返回的结果集，按照时间正序排序
     *
     * 当记录不存在时，返回 size=0 的集合
     * @author lihy  v1.0   2018/7/3
     */
    List<SMSRecord> getSubList(List<SMSRecord> source, Date start, Date end);

    /**
     * 保存短信发送记录，返回受影响行数
     * @author lihy  v1.0   2018/7/10
     */
    int save(String phone, String channel);
}
