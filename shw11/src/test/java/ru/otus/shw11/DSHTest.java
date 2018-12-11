package ru.otus.shw11;

import org.junit.Before;
import org.junit.Test;
import ru.otus.shw10.base.DBService;
import ru.otus.shw10.data.AddressDataSet;
import ru.otus.shw10.data.PhoneDataSet;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw11.dbService.DBServiceHibernateImpl;

import java.util.Arrays;

import static org.junit.Assert.*;

public class DSHTest {

    DBService dbSrv;

    @Before
    public void initHibernate() {
        dbSrv = new DBServiceHibernateImpl();
    }

    @Test
    public void test1_save_read_basics() throws Exception{

        UserDataSet user = new UserDataSet();
        user.setName("Unnamed1");
        user.setAge(40);

        PhoneDataSet phone1 = new PhoneDataSet();
        phone1.setPhone("89437500298");
        phone1.setUserId(1L);
        phone1.setUser(user);

        PhoneDataSet phone2 = new PhoneDataSet();
        phone2.setPhone("89437500298");
        phone2.setUserId(1L);
        phone2.setUser(user);

        user.setPhones(Arrays.asList(phone1, phone2));

        user.setAdress(new AddressDataSet("Большая Ордынка"));

        dbSrv.save(user);

        UserDataSet readUser = dbSrv.read(1);

        Thread.sleep(50);

        System.out.println("INITIAL USER = " + user);
        System.out.println("READ USER = " + readUser);
        assertEquals(user.getName(), readUser.getName());
        assertEquals(user.getAge(), readUser.getAge());

        assertEquals(user.getAdress().getStreet(), readUser.getAdress().getStreet());

        assertEquals(user.getPhones().size(), readUser.getPhones().size());
        assertEquals(user.getPhones().get(0).getPhone(), readUser.getPhones().get(0).getPhone());

    }

}
