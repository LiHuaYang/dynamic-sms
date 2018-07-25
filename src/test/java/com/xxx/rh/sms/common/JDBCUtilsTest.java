package com.xxx.rh.sms.common;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lihy
 * @version 1.0  2018/7/3
 */
public class JDBCUtilsTest {

    @Test
    public void add() throws SQLException {
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "insert into SMSRecord(PhoneNum,Channel) values(?, ?)";
        Object params[] = {"15728046336", "1"};
        Assert.assertEquals(1, qr.update(sql, params));
    }

    @Test
    public void delete() throws SQLException {
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "delete from SMSRecord where id=?";
        qr.update(sql, 1);
    }

    @Test
    public void update() throws SQLException {
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "update SMSRecord set name=? where id=?";
        Object params[] = { "ddd", 5};
        qr.update(sql, params);
    }

    @Test
    public void find() throws SQLException {
//        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
//        String sql = "select * from SMSRecord where id=?";
//        Object params[] = {2};
//        User user = (User) qr.query(sql, params, new BeanHandler(User.class));
    }

    @Test
    public void getAll() throws SQLException {
//        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
//        String sql = "select * from SMSRecord";
//        List list = (List) qr.query(sql, new BeanListHandler(User.class));
//        System.out.println(list.size());
    }

    /**
     * @Method: testBatch
     * @Description:批处理
     * @Anthor:孤傲苍狼
     *
     * @throws SQLException
     */
    @Test
    public void testBatch() throws SQLException {
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "insert into SMSRecord(name,password,email,birthday) values(?,?,?,?)";
        Object params[][] = new Object[10][];
        for (int i = 0; i < 10; i++) {
            params[i] = new Object[] { "aa" + i, "123", "aa@sina.com",
                    new Date() };
        }
        qr.batch(sql, params);
    }

    //用dbutils完成大数据（不建议用）
    /***************************************************************************
     create table testclob
     (
     id int primary key auto_increment,
     resume text
     );
     **************************************************************************/
    @Test
    public void testclob() throws SQLException, IOException {
//        QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
//        String sql = "insert into testclob(resume) values(?)";  //clob
//        //这种方式获取的路径，其中的空格会被使用“%20”代替
//        String path  = QueryRunnerCRUDTest.class.getClassLoader().getResource("data.txt").getPath();
//        //将“%20”替换回空格
//        path = path.replaceAll("%20", " ");
//        FileReader in = new FileReader(path);
//        char[] buffer = new char[(int) new File(path).length()];
//        in.read(buffer);
//        SerialClob clob = new SerialClob(buffer);
//        Object params[] = {clob};
//        runner.update(sql, params);
    }

    @Test
    public void testArrayHandler() throws SQLException{
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "select * from SMSRecord t where t.phoneNum=? and t.channel=?";
        Object result[] = (Object[]) qr.query(sql, new ArrayHandler(), new Object[]{"15728046336", "1"});
        System.out.println(Arrays.asList(result));
    }

    @Test
    public void testArrayListHandler() throws SQLException{
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "select * from SMSRecord t where t.phoneNum=? and t.channel=?";
        List<Object[]> list = (List) qr.query(sql, new ArrayListHandler(), new Object[]{"15728046336", "1"});
        for(Object[] o : list){
            System.out.println(Arrays.asList(o));
        }
    }

    @Test
    public void testColumnListHandler() throws SQLException{
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "select * from SMSRecord";
        List list = (List) qr.query(sql, new ColumnListHandler("id"));
        System.out.println(list);
    }

    @Test
    public void testKeyedHandler() throws Exception{
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "select * from SMSRecord";

        Map<Integer,Map> map = (Map) qr.query(sql, new KeyedHandler("id"));
        for(Map.Entry<Integer, Map> me : map.entrySet()){
            int  id = me.getKey();
            Map<String,Object> innermap = me.getValue();
            for(Map.Entry<String, Object> innerme : innermap.entrySet()){
                String columnName = innerme.getKey();
                Object value = innerme.getValue();
                System.out.println(columnName + "=" + value);
            }
            System.out.println("----------------");
        }
    }

    @Test
    public void testMapHandler() throws SQLException{

        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "select * from SMSRecord";

        Map<String,Object> map = (Map) qr.query(sql, new MapHandler());
        for(Map.Entry<String, Object> me : map.entrySet())
        {
            System.out.println(me.getKey() + "=" + me.getValue());
        }
    }


    @Test
    public void testMapListHandler() throws SQLException{
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "select * from SMSRecord";
        List<Map> list = (List) qr.query(sql, new MapListHandler());
        for(Map<String,Object> map :list){
            for(Map.Entry<String, Object> me : map.entrySet())
            {
                System.out.println(me.getKey() + "=" + me.getValue());
            }
        }
    }

    @Test
    public void testScalarHandler() throws SQLException{
        QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "select count(*) from SMSRecord";  //[13]  list[13]
        int count = ((Long)qr.query(sql, new ScalarHandler(1))).intValue();
        System.out.println(count);
    }
}