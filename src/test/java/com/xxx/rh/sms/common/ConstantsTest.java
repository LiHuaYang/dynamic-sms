package com.xxx.rh.sms.common;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author lihy
 * @version 1.0  2018/7/3
 */
public class ConstantsTest {

    private Logger logger = Logger.getLogger(ConstantsTest.class);

    @Test
    public void test1() throws SQLException {
        Map<String, String> map = new HashMap<>();
        System.out.println(map.get(null));
    }

}