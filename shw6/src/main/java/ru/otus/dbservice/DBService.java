package ru.otus.dbservice;

import ru.otus.data.TableData;

public interface DBService {

    TableData getById(int id);

    int insertInto(TableData td);

}
