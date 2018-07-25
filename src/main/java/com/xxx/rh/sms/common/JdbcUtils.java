package com.xxx.rh.sms.common;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lihy
 * @version 1.0  2018/7/3
 */
public final class JdbcUtils {

    private static ComboPooledDataSource ds = null;

    private static Logger logger = Logger.getLogger(JdbcUtils.class);

    private JdbcUtils(){}

    static {
        try {
            ds = new ComboPooledDataSource();
            ds.setDriverClass("oracle.jdbc.driver.OracleDriver");
            ds.setJdbcUrl("jdbc:oracle:thin:@10.60.45.220:1521:dbtest");
            ds.setUser("MSP_2018");
            ds.setPassword("MSP_2018");
            ds.setInitialPoolSize(10);
            ds.setMinPoolSize(20);
            ds.setMaxPoolSize(50);
            // 通过读取C3P0的xml配置文件创建数据源，C3P0的xml配置文件c3p0-config.xml必须放在src目录下
            // ds = new ComboPooledDataSource();//使用C3P0的默认配置来创建数据源
        } catch (Exception e) {
            logger.error("初始化数据库连接池异常，System.exit(0)", e);
            System.exit(0);
        }
    }

    public static DataSource getDataSource() {
        return ds;
    }

    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public static void startTransaction() {
        try {
            Connection conn = getConnection();
            if (conn == null) {
                conn = getConnection();
            }
            conn.setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void rollback() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                conn.rollback();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void commit() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                conn.commit();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void close() {
        try {
            //从当前线程中获取Connection
            Connection conn = getConnection();
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
