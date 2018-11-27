package ru.otus.shw10.executor;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassWalkThrough {

    public enum SQLType {SELECT, INSERT, UPDATE};

    private final String SELECT = "select %s from %s where id = %s";
    private final String INSERT = "insert into %s (%s) values (%s)";
    private final String UPDATE = "update table %s set %s where id = %d";

    private SQLType type;

    private List<String> sqls = new ArrayList<>();

    private static boolean isPrimitive(Object o) {
        return o instanceof Long || o instanceof Integer || o instanceof Float || o instanceof Boolean
                || o instanceof Short || o instanceof Byte || o instanceof Character || o instanceof Double;
    }

    private Map<String, Object> walkThrough(Object obj) {
                Map<String, Object> fieldPairs = new HashMap<>();
                List<Field> fields = Stream.concat(Arrays.stream(obj.getClass().getDeclaredFields()),
                        Arrays.stream(obj.getClass().getSuperclass().getDeclaredFields())).collect(Collectors.toList());
                for (Field field : fields) {
                    try {
                        field.setAccessible(true);
                        Object subObj = field.get(obj);
                        if(isPrimitive(subObj) || (subObj instanceof String) ){
                            if(subObj instanceof Number) {
                                fieldPairs.put(field.getName(), subObj);
                            }
                            else {
                                fieldPairs.put(field.getName(), '\'' + subObj.toString() + '\'');
                            }
                        } else {
                            continue;
                        }
                    } catch (IllegalAccessException iae) {
                        System.err.println("Failed to serialize " + field.getName() + ": " + iae.getMessage());
                        return null;
                    }
                }
                return fieldPairs;
    }

    public String toSql(Object obj, SQLType type) {
        if (obj == null) return null;
        else {
            return mapToSql(obj.getClass().getSimpleName(), walkThrough(obj), type);
        }
    }

    private String mapToSql(String tableName, Map<String, Object> fields, SQLType type){
        List<String> fieldNames = fields.entrySet().stream().filter(ent -> !ent.getKey().equals("id")).map(Map.Entry::getKey).collect(Collectors.toList());
        List<String> values = fields.entrySet().stream().filter(ent -> !ent.getKey().equals("id")).map(ent -> ent.getValue().toString()).collect(Collectors.toList());
        if(fieldNames.size() == values.size() && fieldNames.size() > 0){
            switch (type){
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
