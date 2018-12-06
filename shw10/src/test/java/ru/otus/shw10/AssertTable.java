package ru.otus.shw10;

import lombok.RequiredArgsConstructor;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class AssertTable {

    private final Connection conn;

    public void assertSelectContain(String sql, Map<String, String> columnValue){
        try(Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                AtomicInteger matchCount = new AtomicInteger(columnValue.size());
                columnValue.forEach((key, value) -> {
                    try {
                        if(rs.getString(key).equals(value))
                            matchCount.getAndDecrement();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                if(matchCount.get() == 0) return;
            }
            fail(sql + " return 0 matches with " + columnValue.toString());
        } catch (SQLException sqle){
            fail(sqle.getMessage());
        }
    }


    public void assertContainValue(Object targetValue, Class columnType, String columnName){

    }

}
