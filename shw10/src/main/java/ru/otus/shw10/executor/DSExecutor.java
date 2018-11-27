package ru.otus.shw10.executor;

import lombok.RequiredArgsConstructor;
import ru.otus.shw10.data.DataSet;

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

    private final String SELECT = "select %s from %s where id = %s";
    private final String INSERT = "insert into %s (%s) values (%s)";
    private final String UPDATE = "update table %s set %s where id = %d";

    public <T extends DataSet> void save(T user) {
        try (Statement stmt = connection.createStatement()) {
            System.out.println(new ClassWalkThrough().toSql(user, ClassWalkThrough.SQLType.INSERT));
            connection.createStatement().executeUpdate(new ClassWalkThrough().toSql(user, ClassWalkThrough.SQLType.INSERT));
        } catch (SQLException sqle) {
            System.err.println("FAILED TO SAVE USER " + user + ": " + sqle.getMessage());
        }
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) {

//        connection.createStatement().executeUpdate(new ClassWalkThrough().toSql(user, ClassWalkThrough.SQLType.SELECT));
        try {
            T ds = (T) Class.forName(clazz.getName()).newInstance();
            Map<String, Object> fieldPairs = new HashMap<>();
            List<Field> fields = Stream.concat(Arrays.stream(ds.getClass().getDeclaredFields()),
                    Arrays.stream(ds.getClass().getSuperclass().getDeclaredFields())).collect(Collectors.toList());
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                fieldPairs.put(Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1), null /*'\'' + subObj.toString() + '\''*/);
            }
            List<String> fieldNames = fieldPairs.entrySet().stream()
                    .filter(ent -> !ent.getKey().equals("id")).map(Map.Entry::getKey).collect(Collectors.toList());

            String SQL = String.format(SELECT, String.join(",", fieldNames), ds.getClass().getSimpleName(), id);
            System.out.println("PAIRS: " + fieldPairs.toString() + ", SQL = " + SQL);
            try {
                ResultSet rs = connection.createStatement().executeQuery(SQL);
                if (rs.next()) {
                    for (String field : fieldNames) {
                        Object fo = rs.getObject(field);
                        System.out.println("SET " + field + ", VALUE = " + fo.toString());
                        for (Method method : ds.getClass().getMethods()) {
                            if (method.getName().equals("set" + field)) {
                                System.out.println("call a setter");
                                try {
                                    method.invoke(ds, fo);
                                } catch (InvocationTargetException ite) {
                                    ite.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } catch (SQLException sqle) {
                System.err.println(sqle.getMessage());
            }
            return ds;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private static boolean isPrimitive(Object o) {
        return o instanceof Long || o instanceof Integer || o instanceof Float || o instanceof Boolean
                || o instanceof Short || o instanceof Byte || o instanceof Character || o instanceof Double;
    }

    private String mapToSql(String tableName, Map<String, Object> fields, ClassWalkThrough.SQLType type) {
        List<String> fieldNames = fields.entrySet().stream().filter(ent -> !ent.getKey().equals("id")).map(Map.Entry::getKey).collect(Collectors.toList());
        List<String> values = fields.entrySet().stream().filter(ent -> !ent.getKey().equals("id")).map(ent -> ent.getValue().toString()).collect(Collectors.toList());
        if (fieldNames.size() == values.size() && fieldNames.size() > 0) {
            switch (type) {
                case INSERT:
                    return String.format(INSERT, tableName, String.join(",", fieldNames), String.join(",", values));

                case SELECT:
                    return String.format(SELECT, String.join(",", fieldNames), tableName, fields.get("id"));

                case UPDATE:
                    List<String> pairs = fields.entrySet().stream()
                            .filter(ent -> !ent.getKey().equals("id"))
                            .map(ent -> ent.getKey() + "=" + ent.getValue().toString())
                            .collect(Collectors.toList());
                    return String.format(UPDATE, tableName, String.join(",", pairs), fields.get("id"));
            }
        }
        throw new IllegalArgumentException(tableName + "/" + fields.toString());
    }

}
