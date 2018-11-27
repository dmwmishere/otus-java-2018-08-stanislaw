package ru.otus.shw10;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.otus.shw10.connection.ConnectionHelper;
import ru.otus.shw10.data.DataSet;
import ru.otus.shw10.data.PhoneDataSet;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw10.executor.ClassWalkThrough;
import ru.otus.shw10.executor.DSExecutor;

import java.sql.*;

public class JDBCTests {

    private static Connection conn;

    @BeforeClass
    public static void initDB() throws SQLException {
        conn = ConnectionHelper.getConnection();
        assert conn != null;
        conn.createStatement().executeUpdate("create table userdataset ( " +
                "id bigint not null generated always as identity (start with 1, increment by 1), " +
                "name varchar(255), " +
                "age smallint " +
                ")");
        conn.createStatement().executeUpdate("create table phonedataset( " +
                "id bigint not null generated always as identity (start with 1, increment by 1), " +
                "phone varchar(11), " +
                "userid bigint not null )");

        conn.createStatement().executeUpdate(
                "insert into userdataset (name, age) values ('user1', 30),('user2', 31)");
        conn.createStatement().executeUpdate(
                "insert into phonedataset (phone, userid) values ('12345678901', 1)");

    }

    @Test
    public void test0_getData() throws SQLException {
        assert conn!=null;

        try(Statement stmt = conn.createStatement()){
            stmt.execute("select * from userdataset");
            stmt.execute("select * from phonedataset");
        }


    }

    @Test
    public void test1_insert(){
        ClassWalkThrough cwt = new ClassWalkThrough();
        UserDataSet user = new UserDataSet();
        user.setName("Unnamed1");
        user.setAge(40);
        user.setId(666);
        System.out.println(cwt.toSql(user, ClassWalkThrough.SQLType.INSERT));

        PhoneDataSet phone = new PhoneDataSet();
        phone.setPhone("89437500298");
        phone.setUserId(666);
        phone.setId(666);
        System.out.println(cwt.toSql(phone, ClassWalkThrough.SQLType.INSERT));

    }

    @Test
    public void test2_select(){
        ClassWalkThrough cwt = new ClassWalkThrough();
        UserDataSet user = new UserDataSet();
        user.setName("Unnamed1");
        user.setAge(40);
        user.setId(4);
        System.out.println(cwt.toSql(user, ClassWalkThrough.SQLType.SELECT));

        PhoneDataSet phone = new PhoneDataSet();
        phone.setPhone("89437500298");
        phone.setUserId(666);
        phone.setId(2);
        System.out.println(cwt.toSql(phone, ClassWalkThrough.SQLType.SELECT));

    }

    @Test
    public void test3_update(){
        ClassWalkThrough cwt = new ClassWalkThrough();
        UserDataSet user = new UserDataSet();
        user.setName("Unnamed1");
        user.setAge(40);
        user.setId(666);
        System.out.println(cwt.toSql(user, ClassWalkThrough.SQLType.UPDATE));

        PhoneDataSet phone = new PhoneDataSet();
        phone.setPhone("89437500298");
        phone.setUserId(666);
        phone.setId(666);
        System.out.println(cwt.toSql(phone, ClassWalkThrough.SQLType.UPDATE));

    }

    @Test
    public void test4_save2db(){
        ClassWalkThrough cwt = new ClassWalkThrough();
        UserDataSet user = new UserDataSet();
        user.setName("Unnamed1");
        user.setAge(40);
        user.setId(4);

        PhoneDataSet phone = new PhoneDataSet();
        phone.setPhone("89437500298");
        phone.setUserId(666);
        phone.setId(2);

        DSExecutor dsExecutor = new DSExecutor(conn);

        dsExecutor.save(user);

        dsExecutor.save(phone);

        printTable("userdataset");

        printTable("phonedataset");

    }

    @Test
    public void test5_load(){
        DSExecutor dsExecutor = new DSExecutor(conn);
        DataSet ds = dsExecutor.load(1, PhoneDataSet.class);
        DataSet ds2 = dsExecutor.load(1, UserDataSet.class);
        assert ds != null;
        assert ds2 != null;
        System.out.println(ds);
        System.out.println(ds2);
    }

    private void printTable(String table){
        try {
            ResultSet rs = conn.createStatement().executeQuery("select * from " + table);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                System.out.print("| ");
                for (int i = 1; i <= columnsNumber; i++) {
                    System.out.print(rs.getString(i) + " | ");
                }
                System.out.println();
            }
        } catch (SQLException sqle){
            System.err.println("FAILED TO PRINT " + table + ": " + sqle.getMessage());
        }
    }

}
