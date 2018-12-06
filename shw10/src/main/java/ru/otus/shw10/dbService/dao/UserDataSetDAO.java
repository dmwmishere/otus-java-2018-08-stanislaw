package ru.otus.shw10.dbService.dao;

import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw10.executor.DSExecutor;

import java.sql.Connection;

public class UserDataSetDAO {

    private final DSExecutor dse;

    public UserDataSetDAO(Connection connection) {
        this.dse = new DSExecutor(connection);
    }

    public void save(UserDataSet dataSet) {
        dse.save(dataSet);
    }

    public UserDataSet read(long id) {
        return dse.load(id, UserDataSet.class);
    }
}
