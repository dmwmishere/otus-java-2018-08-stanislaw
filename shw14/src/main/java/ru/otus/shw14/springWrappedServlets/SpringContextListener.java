package ru.otus.shw14.springWrappedServlets;

import com.github.javafaker.Faker;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.shw10.base.DBService;
import ru.otus.shw10.data.AddressDataSet;
import ru.otus.shw10.data.PhoneDataSet;
import ru.otus.shw10.data.UserDataSet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SpringContextListener implements ServletContextListener {

    private static ApplicationContext ctx;

    public static <T> T getSpringBean(String beanName, Class<T> clazz){
        if(ctx == null){
            return null;
        }
        return ctx.getBean(beanName, clazz);
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Init spring context...");
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        populateWithTestData(getSpringBean("dbSrv", DBService.class), 20);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

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
