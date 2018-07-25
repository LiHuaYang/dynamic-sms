package com.xxx.rh.sms.support;

import com.xxx.rh.sms.bean.BlackListItem;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 手机号黑名单，用内存管理实现
 * @author lihy
 * @version 1.0  2018/7/4
 */
public class BlackListCacheManager implements BlackListManager {

    /** k：手机号 v：下次可用时间 */
    private ConcurrentHashMap<String, BlackListItem> map = new ConcurrentHashMap<>();

    @Override
    public BlackListItem get(String phone) {
        return map.get(phone);
    }

    @Override
    public void add(BlackListItem item) {
        map.put(item.getPhoneNum(), item);
    }

    @Override
    public void del(String phone) {
        map.remove(phone);
    }

    @Override
    public boolean onBlackList(String phone) {
        return map.containsKey(phone);
    }

    @Override
    public boolean isExpired(String phone) {
        if (get(phone) == null) {
            return false;
        }
        // 当前时间早于过期时间
        if (new Date().compareTo(get(phone).getExpiredTime()) <= 0) {
            return false;
        }
        return true;
    }
}
