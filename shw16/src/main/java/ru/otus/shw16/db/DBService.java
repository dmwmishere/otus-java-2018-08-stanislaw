package ru.otus.shw16.db;

import ru.otus.shw16.app.Addressee;
import ru.otus.shw16.app.messages.user.UserAddRq;
import ru.otus.shw16.app.messages.user.UserReportRq;

/**
 * Created by tully.
 */
public interface DBService extends Addressee {
    void init();
    void save(UserAddRq userAddRq);
    void userReport(UserReportRq userReportRq);
}
