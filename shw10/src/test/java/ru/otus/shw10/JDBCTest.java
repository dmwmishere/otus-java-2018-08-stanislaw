package ru.otus.shw10;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.otus.shw10.connection.ConnectionHelper;
import ru.otus.shw10.data.ExtendedUserDataSet;
import ru.otus.shw10.data.PhoneDataSet;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw10.executor.DSExecutor;
import ru.otus.shw10.reflection.ReflectionHelper;
import ru.otus.shw6.engine.CacheEngine;
import ru.otus.shw6.engine.MyCacheEngine;

import java.sql.*;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class JDBCTest {

    private static Connection conn;

    @BeforeClass
    public static void initDB() throws SQLException {
        conn = ConnectionHelper.getConnection();
        assertNotNull(conn);
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
        assertNotNull(conn);

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

        new AssertTable(conn).assertSelectContain("select * from userdataset", Collections.singletonMap("name", "Unnamed1"));

        new AssertTable(conn).assertSelectContain("select * from phonedataset", Collections.singletonMap("phone", "89437500298"));
    }

    @Test
    public void test2_load(){
        DSExecutor dsExecutor = new DSExecutor(conn);
        PhoneDataSet ds = dsExecutor.load(1, PhoneDataSet.class);
        UserDataSet ds2 = dsExecutor.load(2, UserDataSet.class);
        System.out.println(ds);
        System.out.println(ds2);

        assertNotNull(ds);
        assertNotNull(ds2);
        assertEquals("12345678901", ds.getPhone());
        assertEquals("user2", ds2.getName());

    }

    @Test
    public void test3_reflection_helpers() throws Exception{
        UserDataSet user = new UserDataSet();
        user.setName("Unnamed1");
        user.setAge(40);
        user.setId(4L);
        ReflectionHelper.reflectionSetter(user, "name", "Another Name");
        System.out.println("Change name = " + ReflectionHelper.reflectionGetter(user, "name"));
        assertEquals("Another Name", ReflectionHelper.reflectionGetter(user, "name"));

        ReflectionHelper.reflectionSetter(user, "age", 18);
        System.out.println("Change age = " + ReflectionHelper.reflectionGetter(user, "age"));
        assertEquals(18, ReflectionHelper.reflectionGetter(user, "age"));

        System.out.println(ReflectionHelper.getFieldsList(user));

        assertTrue(ReflectionHelper.getFieldsList(user).contains("name"));
        assertTrue(ReflectionHelper.getFieldsList(user).contains("id"));
        assertTrue(ReflectionHelper.getFieldsList(user).contains("age"));
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

        assertEquals(0, exUserLoad.getGender());
        assertEquals("SameName", exUserLoad.getName());
    }

    @Test
    public void test5_cached_fields(){
        DSExecutor dsExecutor = new DSExecutor(conn);

        for(int i = 0; i < 10; i++){
            UserDataSet user = new UserDataSet();
            user.setName("Unnamed" + i);
            user.setAge(40 + i);
            dsExecutor.save(user);
        }


        printTable("userdataset");

        printTable("phonedataset");

        AssertTable at = new AssertTable(conn);

        for(int i = 0; i < 10; i++){
            at.assertSelectContain("select * from userdataset", Collections.singletonMap("name", "Unnamed" + i));
            at.assertSelectContain("select * from userdataset", Collections.singletonMap("age", Integer.toString(40+i)));
        }

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
