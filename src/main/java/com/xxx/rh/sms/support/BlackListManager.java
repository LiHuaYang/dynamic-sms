package com.xxx.rh.sms.support;

import com.xxx.rh.sms.bean.BlackListItem;

/**
 * 黑名单管理接口
 * @author lihy
 * @version 1.0  2018/7/4
 */
public interface BlackListManager {

    /**
     * 获取手机号对应黑名单信息
     * @author lihy  v1.0   2018/7/4
     */
    BlackListItem get(String phone);

    /**
     * 将手机号码和下次可用时间放进管理器中
     * @author lihy  v1.0   2018/7/4
     */
    void add(BlackListItem item);

    /**
     * 将手机号码和下次可用时间放进管理器中
     * @author lihy  v1.0   2018/7/4
     */
    void del(String phone);

    /**
     * 是否在黑名单上
     * @author lihy  v1.0   2018/7/4
     */
    boolean onBlackList(String phone);

    /**
     * 条目是否过期
     * @author lihy  v1.0   2018/7/4
     */
    boolean isExpired(String phone);
}
