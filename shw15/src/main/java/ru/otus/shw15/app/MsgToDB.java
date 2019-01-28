package ru.otus.shw15.app;

import ru.otus.shw15.db.DBService;
import ru.otus.shw15.messageSystem.Address;
import ru.otus.shw15.messageSystem.Addressee;
import ru.otus.shw15.messageSystem.Message;

/**
 * Created by tully.
 */
public abstract class MsgToDB extends Message {
    public MsgToDB(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DBService) {
            exec((DBService) addressee);
        }
    }

    public abstract void exec(DBService dbService);
}
