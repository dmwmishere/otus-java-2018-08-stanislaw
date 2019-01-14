package ru.otus.shw11.dbService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.otus.shw10.base.DBService;
import ru.otus.shw10.data.AddressDataSet;
import ru.otus.shw10.data.PhoneDataSet;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw11.dbService.dao.UserDataSetDAO;
import ru.otus.shw6.engine.CacheEngine;
import ru.otus.shw6.engine.MyCacheEngine;

import java.util.List;
import java.util.function.Function;

public class DBServiceHibernateImpl implements DBService {

    private final SessionFactory sessionFactory;

    private final CacheEngine<Long, UserDataSet> cache;

    /**
     * use default cache configuration if no caching service specified
     */
    public DBServiceHibernateImpl(){
        this(new MyCacheEngine(1000));
    }

    public DBServiceHibernateImpl(CacheEngine cache) {
        this.cache = cache;

        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(PhoneDataSet.class);
        configuration.addAnnotatedClass(AddressDataSet.class);

        // mySQL connection settings:
        //docker run -p 3306:3306 --name cmysql -e MYSQL_ROOT_PASSWORD=test -d mysql
//        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
//        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
//        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://0.0.0.0:3306/testdb");
//
//        configuration.setProperty("hibernate.connection.username", "root");
//        configuration.setProperty("hibernate.connection.password", "test");

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.DerbyTenSixDialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.apache.derby.jdbc.EmbeddedDriver");
        configuration.setProperty("hibernate.connection.url", "jdbc:derby:memory:testDB;create=true");

        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.connection.useSSL", "false");
        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");

        sessionFactory = createSessionFactory(configuration);
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public String getLocalStatus() {
        return runInSession(session -> session.getTransaction().getStatus().name());
    }

    public void save(UserDataSet dataSet) {
        // as long as add form redirects to browser form this will get into cache as well
        runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            dao.save(dataSet);
            return 1;
        });
    }

    public UserDataSet read(long id) {

        if(cache.get(id) == null) {
            UserDataSet userDataSet = runInSession(session -> {
                UserDataSetDAO dao = new UserDataSetDAO(session);
                return dao.read(id);
            });
            cache.put(id, userDataSet);
            System.out.println("PUT " + id + " INTO CACHE");
            return userDataSet;
        } else {
            System.out.println("GET " + id + " FROM CACHE");
            return cache.get(id);
        }
    }

    @Override
    public UserDataSet read(String name) {
        if(cache.get((long)name.hashCode()) == null) {
            UserDataSet userDataSet = runInSession(session -> {
                UserDataSetDAO dao = new UserDataSetDAO(session);
                return dao.readByName(name);
            });
            cache.put((long)name.hashCode(), userDataSet);
            System.out.println("PUT " + name + " INTO CACHE");
            return userDataSet;
        } else {
            System.out.println("GET " + name + " FROM CACHE");
            return cache.get((long)name.hashCode());
        }
    }

    public List<UserDataSet> readAll() {
        List<UserDataSet> users = runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.readAll();
        });

        // update cache for each user by id,
        // caching by name implemented on search level only
        users.forEach(user -> cache.put(user.getId(), user));
        System.out.println("CACHE UPDATED!");
        return users;
    }

    public void shutdown() {
        sessionFactory.close();
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }

    @Override
    public void close() throws Exception {
        sessionFactory.close();
    }
}
