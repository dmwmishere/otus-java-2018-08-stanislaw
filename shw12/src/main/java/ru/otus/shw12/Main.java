package ru.otus.shw12;

/*
ДЗ-12: Веб сервер
Встроить веб сервер в приложение из ДЗ-11.
Сделать админскую страницу, на которой можно добавить пользователя,
получить имя пользователя по id и получить количество пользователей в базе.
*/

import com.github.javafaker.Faker;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
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

    public static void main(String [] argz) throws Exception {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        DBService dbSrv = new DBServiceHibernateImpl();

        // populate db with test data
        UserDataSet user;
        AddressDataSet address;
        Random r = new Random();
        Faker faker = new Faker(new Locale("ru"));
        for(int i = 0; i<r.nextInt(10) + 15; i++){
            user = new UserDataSet();
            user.setName(faker.name().fullName());
            user.setAge(r.nextInt(30) + 16);
            address = new AddressDataSet();
            address.setStreet(faker.address().streetAddress());
            List<PhoneDataSet> phones = new ArrayList<>();
            for(int j = 0; j < r.nextInt(4); j++){
                PhoneDataSet phone = new PhoneDataSet();
                phone.setPhone(faker.phoneNumber().phoneNumber());
                phones.add(phone);
                phone.setUser(user);
            }
            if(phones.size() > 0){
                user.setPhones(phones);
            }
            user.setAdress(address);
            System.out.println(user);
            dbSrv.save(user);
        }

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();

        context.addServlet(new ServletHolder(new AddUserServlet(dbSrv)), "/useradd");
        context.addServlet(new ServletHolder(new SearchUserServlet(dbSrv, templateProcessor)), "/searchuser");
        context.addServlet(new ServletHolder(new BrowseUserServlet(dbSrv, templateProcessor)), "/browseuser");
        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();
    }

}
