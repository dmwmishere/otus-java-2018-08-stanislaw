package ru.otus.shw10.executor;

import lombok.RequiredArgsConstructor;
import ru.otus.shw10.data.DataSet;
import ru.otus.shw10.reflection.ReflectionHelper;
import ru.otus.shw6.engine.CacheEngine;
import ru.otus.shw6.engine.MyCacheEngine;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DSExecutor {

    private final Connection connection;

    private CacheEngine<String, List<String> > cache = new MyCacheEngine<>(1000);

    private final String SELECT = "select %s from %s where id = %s";
    private final String INSERT = "insert into %s (%s) values (%s)";

    public <T extends DataSet> void save(T user) {
        try (Statement stmt = connection.createStatement()) {
            List<String> fieldNames = ReflectionHelper.getFieldsList(user, cache);
            fieldNames.remove("id");
            List<String> values = fieldNames.stream().map(field -> {
                try {
                    Object value = ReflectionHelper.reflectionGetter(user, field);
                    if(value instanceof Number)
                        return value.toString();
                    else
                        return '\'' + value.toString() + '\'';
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            return null; } ).collect(Collectors.toList());
            String SQL = String.format(INSERT, user.getClass().getSimpleName(), String.join(",", fieldNames), String.join(",", values));
            System.out.println("INSERT SQL = " + SQL);
            stmt.executeUpdate(SQL);
        } catch (SQLException sqle) {
            System.err.println("FAILED TO SAVE USER " + user + ": " + sqle.getMessage());
        }
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) {
        try {
            T ds = clazz.getDeclaredConstructor().newInstance();
            List<String> fields = ReflectionHelper.getFieldsList(ds);
            String SQL = String.format(SELECT, String.join(",", fields), ds.getClass().getSimpleName(), id);
            System.out.println("SQL = " + SQL);
            try {
                ResultSet rs = connection.createStatement().executeQuery(SQL);
                if (rs.next()) {
                    for (String field : fields) {
                        try {
                            Object fo = rs.getObject(field);
                            System.out.println("SET " + field + ", VALUE = " + fo.toString());
                            ReflectionHelper.reflectionSetter(ds, field, fo);
                        } catch (InvocationTargetException | NoSuchMethodException ite) {
                            ite.printStackTrace();
                            break;
                        }
                    }
                }
            } catch (SQLException sqle) {
                System.err.println(sqle.getMessage());
            }
            return ds;
        } catch (InvocationTargetException |
                NoSuchMethodException |
                InstantiationException |
                IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
