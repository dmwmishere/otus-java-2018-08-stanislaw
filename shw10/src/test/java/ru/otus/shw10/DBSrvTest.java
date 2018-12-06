package ru.otus.shw10;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.otus.shw10.base.DBService;
import ru.otus.shw10.data.AddressDataSet;
import ru.otus.shw10.data.PhoneDataSet;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw10.dbService.DBServiceImpl;

import java.util.Arrays;

import static org.junit.Assert.*;

public class DBSrvTest {

    DBService dbs = null;

    @Before
    public void initDBService(){
            dbs = new DBServiceImpl();
    }

    @After
    public void destroyDBService() throws Exception{
        dbs.close();
    }

    @Test
    public void test0_saveUser_dbService(){

        UserDataSet user = new UserDataSet();
        user.setName("Unnamed1");
        user.setAge(40);

        dbs.save(user);

        UserDataSet readUser = dbs.read(3);

        System.out.println(readUser);

        assertEquals("user1", dbs.read(1).getName());
        assertEquals(user.getName(), readUser.getName());
        assertEquals(user.getAge(), readUser.getAge());

    }

    @Test
    public void test1_one2one(){
        UserDataSet user = new UserDataSet();
        user.setName("Unnamed1");
        user.setAge(40);

        PhoneDataSet phone1 = new PhoneDataSet();
        phone1.setPhone("89437500298");
        phone1.setUserId(3L);

        PhoneDataSet phone2 = new PhoneDataSet();
        phone2.setPhone("89437500298");
        phone2.setUserId(3L);

        user.setPhones(Arrays.asList(phone1, phone2));

        user.setAdress(new AddressDataSet("Большая Якиманка"));

        dbs.save(user);

        UserDataSet readUser = dbs.read(3);
        System.out.println(readUser);

        System.out.println("Initial user: " + user);
        System.out.println("Loaded user:  " + readUser);

        assertEquals(user.getName(), readUser.getName());
        assertEquals(user.getAge(), readUser.getAge());

//        assertEquals(user.getAdress().getStreet(), readUser.getAdress().getStreet());
//
//        assertEquals(user.getPhones().size(), readUser.getPhones().size());
//        assertEquals(user.getPhones().get(0).getPhone(), readUser.getPhones().get(0).getPhone());

    }

}
