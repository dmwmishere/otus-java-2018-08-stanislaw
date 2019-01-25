package ru.otus.shw15.app.messages;

import ru.otus.shw10.base.DBService;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw15.app.MsgToDB;
import ru.otus.shw15.db.DBMessageSystemImpl;
import ru.otus.shw15.messageSystem.Address;

/**
 * Created by tully.
 */
public class UserAddRq extends MsgToDB {
    private final UserDataSet user;

    public UserAddRq(Address from, Address to, UserDataSet user) {
        super(from, to);
        this.user = user;
    }

    @Override
    public void exec(DBService dbService) {
        System.out.println("DB message received");
        dbService.save(user);
        ((DBMessageSystemImpl)dbService).getMS().sendMessage(new UserAddRs(getTo(), getFrom(), "DB - OK"));
    }
}
