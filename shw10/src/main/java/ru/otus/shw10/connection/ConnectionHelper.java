package ru.otus.shw10.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {

    public static Connection getConnection() {
        try {

            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            return DriverManager.getConnection("jdbc:derby:memory:testDB;create=true");

        } catch (SQLException | ClassNotFoundException sqle) {
            throw new RuntimeException(sqle);
        }
    }
}
