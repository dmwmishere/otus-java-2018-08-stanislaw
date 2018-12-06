package ru.otus.shw10.base;

import ru.otus.shw10.data.UserDataSet;

import java.util.List;

public interface DBService extends AutoCloseable {

    void save(UserDataSet dataSet);

    UserDataSet read(long id);

    List<UserDataSet> readAll();

}
