package ru.otus.shw15.db;

import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw11.dbService.DBServiceHibernateImpl;
import ru.otus.shw15.app.MessageSystemContext;
import ru.otus.shw15.messageSystem.Address;
import ru.otus.shw15.messageSystem.Addressee;
import ru.otus.shw15.messageSystem.MessageSystem;

public class DBMessageSystemImpl extends DBServiceHibernateImpl implements Addressee {

    private final Address address;

    private final MessageSystemContext context;

    public DBMessageSystemImpl(MessageSystemContext context, Address address) {
        this.context = context;
        this.address = address;
    }

    public void init() {
        context.getMessageSystem().addAddressee(this);
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void save(UserDataSet dataSet) {
        System.out.println("Saving user to database... " + dataSet.getName());
        super.save(dataSet);
//        try{ // Emulates slow db;
//            Thread.sleep(1000);
//        } catch (InterruptedException ie){
//            ie.printStackTrace();
//        }
    }
}
