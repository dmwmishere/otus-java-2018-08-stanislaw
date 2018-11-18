package ru.otus.shw9;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassWalkThrough {

    private StringBuilder sb = new StringBuilder();

    public static boolean isPrimitive(Object o) {
        return o instanceof Long || o instanceof Integer || o instanceof Float || o instanceof Boolean
                || o instanceof Short || o instanceof Byte || o instanceof Character || o instanceof Double;
    }

    public void walkThrough(Object obj, int level) {
        if (obj == null) {
            return;
        } else {
            if (obj.getClass().isArray()) { // ARRAY
                sb.append('[');
                int array_len = Array.getLength(obj);
                for (int j = 0; j < array_len; j++) {
                    walkThrough(Array.get(obj, j), level + 1);
                    if (j < array_len - 1) sb.append(',');
                }
                sb.append(']');
            } else if (obj instanceof Collection) { // JAVA COLLECTION
                sb.append('[');
                Collection c = (Collection) obj;
                Iterator colElements = c.iterator();
                while (colElements.hasNext()) {
                    walkThrough(colElements.next(), level + 1);
                    if (colElements.hasNext()) sb.append(',');
                }
                sb.append(']');
            } else if (!isPrimitive(obj) && !(obj instanceof String)) { // ANY NON STRING/PRIMITIVE OBJECT
                List<Field> fields = Stream.concat(Arrays.stream(obj.getClass().getDeclaredFields()),
                        Arrays.stream(obj.getClass().getSuperclass().getDeclaredFields())).collect(Collectors.toList());
                Iterator<Field> fieldIter = fields.iterator();
                sb.append('{');
                while (fieldIter.hasNext()) {
                    Field field = fieldIter.next();
                    field.setAccessible(true);
                    sb.append(wrapName(field.getName()));
                    try {
                        walkThrough(field.get(obj), level + 1);
                    } catch (IllegalAccessException iae) {
                        System.err.println("Failed to serialize " + field.getName());
                        return;
                    }
                    if(fieldIter.hasNext()) sb.append(',');
                }
                sb.append('}');
            } else { // OTHER
                sb.append(wrapValue(obj));
            }
        }
    }

    private String wrapName(String name) {
        return '"' + name + "\":";
    }

    private String wrapValue(Object obj) {
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        } else return '"' + obj.toString() + '"';
    }

    public String toJson(Object obj) {
        if (obj == null) return null;
        else {
            walkThrough(obj, 0);
            return sb.toString();
        }
    }

}
