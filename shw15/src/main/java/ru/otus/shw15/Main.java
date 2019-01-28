package ru.otus.shw15;


import com.github.javafaker.Faker;
import org.apache.commons.collections4.MapUtils;
import ru.otus.shw10.base.DBService;
import ru.otus.shw10.data.AddressDataSet;
import ru.otus.shw10.data.PhoneDataSet;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw12.*;
import ru.otus.shw15.app.MessageSystemContext;
import ru.otus.shw15.db.DBServiceImpl;
import ru.otus.shw15.messageSystem.Address;
import ru.otus.shw15.messageSystem.MessageSystem;
import ru.otus.shw15.frontend.WebSocketUserDbServlet;

import java.util.*;

/**
 * ДЗ-15: MessageSystem
 * Добавить систему обмена сообщениями в веб сервер из ДЗ-13.
 * Пересылать сообщения из вебсокета в DBService и обратно.
 * Организовать структуру пакетов без циклических зависимостей.
 */
public class Main {


    private final static int PORT = 8090;
    private final static String PUBLIC_HTML = "public_html";

    public static void main(String[] argz) throws Exception {

        MessageSystem messageSystem = new MessageSystem();

        MessageSystemContext context = new MessageSystemContext(messageSystem);
        Address frontAddress = new Address("Frontend");
        context.setFrontAddress(frontAddress);
        Address dbAddress = new Address("DB");
        context.setDbAddress(dbAddress);

        DBServiceImpl dbSrv = new DBServiceImpl(context, dbAddress);


        TemplateProcessor templateProcessor = new TemplateProcessor();
        UserDBServer uSrv = new UserDBServer(PUBLIC_HTML, PORT, MapUtils.putAll(new HashMap<>(),
                new Object[][]{
                        {new AddUserServlet(dbSrv), "/useradd"},
                        {new SearchUserServlet(dbSrv, templateProcessor), "/searchuser"},
                        {new BrowseUserServlet(dbSrv, templateProcessor), "/browseuser"},
                        {new WebSocketUserDbServlet(frontAddress, context), "/asyncadduser"},
                }));
        uSrv.start();

        dbSrv.init();
        messageSystem.start();

        populateWithTestData(dbSrv, 20);

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
