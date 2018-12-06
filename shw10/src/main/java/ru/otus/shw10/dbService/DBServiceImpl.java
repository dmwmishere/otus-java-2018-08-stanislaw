package ru.otus.shw10.dbService;

import ru.otus.shw10.base.DBService;
import ru.otus.shw10.connection.ConnectionHelper;
import ru.otus.shw10.data.UserDataSet;
import ru.otus.shw10.dbService.dao.UserDataSetDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DBServiceImpl implements DBService {

    Connection connection;

    public DBServiceImpl() {
        connection = ConnectionHelper.getConnection();
        try {
            initDB();
            populateTestDb();
        } catch(SQLException sqle){
            System.err.println("FAILED TO INITIATE DATABASE: " + sqle.getMessage());
        }
    }

    private void initDB() throws SQLException {
        connection.createStatement().executeUpdate("create table userdataset ( " +
                "id bigint not null generated always as identity (start with 1, increment by 1), " +
                "name varchar(255), " +
                "age smallint " +
                ")");
        connection.createStatement().executeUpdate("create table phonedataset( " +
                "id bigint not null generated always as identity (start with 1, increment by 1), " +
                "phone varchar(11), " +
                "userid bigint not null )");
        connection.createStatement().executeUpdate("create table extendeduserdataset( " +
                "id bigint not null generated always as identity (start with 1, increment by 1), " +
                "name varchar(255), " +
                "age smallint, " +
                "gender smallint, " +
                "education varchar(1024) )");
    }

    private void populateTestDb() throws SQLException{
        connection.createStatement().executeUpdate(
                "insert into userdataset (name, age) values ('user1', 30),('user2', 31)");
        connection.createStatement().executeUpdate(
                "insert into phonedataset (phone, userid) values ('12345678901', 1)");
    }

    @Override
    public void save(UserDataSet dataSet) {
        new UserDataSetDAO(connection).save(dataSet);
    }

    @Override
    public UserDataSet read(long id) {
        return new UserDataSetDAO(connection).read(id);
    }

    @Override
    public List<UserDataSet> readAll() {
        throw new UnsupportedOperationException("not supported by DSExecutor");
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
