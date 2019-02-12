package ru.otus.shw16.app.messages.user;

import lombok.extern.slf4j.Slf4j;
import ru.otus.shw16.app.messages.base.MsgToDB;
import ru.otus.shw16.db.DBService;

@Slf4j
public class UserReportRq extends MsgToDB {

    @Override
    public void exec(DBService dbService) {
        dbService.userReport(this);
    }
}
