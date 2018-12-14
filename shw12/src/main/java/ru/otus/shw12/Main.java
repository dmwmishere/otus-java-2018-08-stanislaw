package ru.otus.shw12;

/*
ДЗ-12: Веб сервер
Встроить веб сервер в приложение из ДЗ-11.
Сделать админскую страницу, на которой можно добавить пользователя,
получить имя пользователя по id и получить количество пользователей в базе.
*/

import com.github.javafaker.Faker;
import ru.otus.shw10.base.DBService;
import ru.otus.shw10.data.AddressDataSet;
import ru.otus.shw10.data.PhoneDataSet;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw11.dbService.DBServiceHibernateImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Main {

    private final static int PORT = 8090;
    private final static String PUBLIC_HTML = "public_html";

    public static void main(String[] argz) throws Exception {

        DBService dbSrv = new DBServiceHibernateImpl();

        populateWithTestData(dbSrv, 20);

        UserDBServer uSrv = new UserDBServer(PUBLIC_HTML, PORT, dbSrv);
        uSrv.start();
        uSrv.join();
    }

    private static void populateWithTestData(DBService dbSrv, final int userCount) {
        UserDataSet user;
        AddressDataSet address;
        Random r = new Random();
        Faker faker = new Faker(new Locale("ru"));
        for (int i = 0; i < userCount; i++) {
            user = new UserDataSet();
            user.setName(faker.name().fullName());
            user.setAge(r.nextInt(30) + 16);
            address = new AddressDataSet();
            address.setStreet(faker.address().streetAddress());
            List<PhoneDataSet> phones = new ArrayList<>();
            for (int j = 0; j < r.nextInt(4); j++) {
                PhoneDataSet phone = new PhoneDataSet();
                phone.setPhone(faker.phoneNumber().phoneNumber());
                phones.add(phone);
                phone.setUser(user);
            }
            if (phones.size() > 0) {
                user.setPhones(phones);
            }
            user.setAdress(address);
            System.out.println(user);
            dbSrv.save(user);
        }
    }

}
