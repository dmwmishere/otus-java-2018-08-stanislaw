package ru.otus.shw15.db;

import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw15.messageSystem.Addressee;

/**
 * Created by tully.
 */
public interface DBService extends Addressee {
    void init();
    void save(UserDataSet dataSet);
}
