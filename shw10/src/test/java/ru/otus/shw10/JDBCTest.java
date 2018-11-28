package ru.otus.shw10;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.otus.shw10.connection.ConnectionHelper;
import ru.otus.shw10.data.DataSet;
import ru.otus.shw10.data.ExtendedUserDataSet;
import ru.otus.shw10.data.PhoneDataSet;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw10.executor.DSExecutor;
import ru.otus.shw10.reflection.ReflectionHelper;

import java.sql.*;

public class JDBCTest {

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
        conn.createStatement().executeUpdate("create table extendeduserdataset( " +
                "id bigint not null generated always as identity (start with 1, increment by 1), " +
                "name varchar(255), " +
                "age smallint, " +
                "gender smallint, " +
                "education varchar(1024) )");

        conn.createStatement().executeUpdate(
                "insert into userdataset (name, age) values ('user1', 30),('user2', 31)");
        conn.createStatement().executeUpdate(
                "insert into phonedataset (phone, userid) values ('12345678901', 1)");

    }

    @Test
    public void test0_getData() throws SQLException {
        assert conn!=null;

        try(Statement stmt = conn.createStatement()){
            stmt.executeQuery("select * from userdataset");
            stmt.execute("select * from phonedataset");
        }


    }

    @Test
    public void test1_save2db(){
        UserDataSet user = new UserDataSet();
        user.setName("Unnamed1");
        user.setAge(40);

        PhoneDataSet phone = new PhoneDataSet();
        phone.setPhone("89437500298");
        phone.setUserId(3L);

        DSExecutor dsExecutor = new DSExecutor(conn);

        dsExecutor.save(user);

        dsExecutor.save(phone);

        printTable("userdataset");

        printTable("phonedataset");

    }

    @Test
    public void test2_load(){
        DSExecutor dsExecutor = new DSExecutor(conn);
        PhoneDataSet ds = dsExecutor.load(1, PhoneDataSet.class);
        UserDataSet ds2 = dsExecutor.load(2, UserDataSet.class);
        System.out.println(ds);
        System.out.println(ds2);
        assert ds != null;
        assert ds2 != null;
        assert ds.getPhone().equals("12345678901");
        assert ds2.getName().equals("user2");
    }

    @Test
    public void test3_reflection_helpers() throws Exception{
        UserDataSet user = new UserDataSet();
        user.setName("Unnamed1");
        user.setAge(40);
        user.setId(4L);
        ReflectionHelper.reflectionSetter(user, "name", "Another Name");
        System.out.println(ReflectionHelper.reflectionGetter(user, "name"));
        assert ReflectionHelper.reflectionGetter(user, "name").equals("Another Name");

        ReflectionHelper.reflectionSetter(user, "age", 18);
        System.out.println(ReflectionHelper.reflectionGetter(user, "age"));
        assert ReflectionHelper.reflectionGetter(user, "age").equals(18);

        System.out.println(ReflectionHelper.getFieldsList(user));
        assert ReflectionHelper.getFieldsList(user).contains("name");
        assert ReflectionHelper.getFieldsList(user).contains("id");
        assert ReflectionHelper.getFieldsList(user).contains("age");
    }

    @Test
    public void test4_extended_user(){
        ExtendedUserDataSet exUser = new ExtendedUserDataSet();
        exUser.setGender(0);
        exUser.setEducation("Specialist");
        exUser.setAge(42);
        exUser.setName("SameName");

        DSExecutor dsExecutor = new DSExecutor(conn);
        dsExecutor.save(exUser);

        ExtendedUserDataSet exUserLoad = dsExecutor.load(1, ExtendedUserDataSet.class);
        System.out.println(exUserLoad);
        assert exUserLoad.getGender() == 0;
        assert exUserLoad.getName().equals("SameName");

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
