package ru.otus.shw15.app.messages;

import ru.otus.shw15.app.FrontendService;
import ru.otus.shw15.app.MsgToFrontend;
import ru.otus.shw15.messageSystem.Address;

/**
 * Created by tully.
 */
public class UserAddRs extends MsgToFrontend {
    private final String dbStatus;

    public UserAddRs(Address from, Address to, String dbStatus) {
        super(from, to);
        this.dbStatus = dbStatus;
    }

    @Override
    public void exec(FrontendService frontendService) {
        System.out.println("FrontEnd message received");
        frontendService.userAddStatus(dbStatus);
    }
}
