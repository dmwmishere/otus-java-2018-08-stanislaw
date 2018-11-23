package ru.otus.shw9;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
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
                int i = 0;
                for (int j = 0; j < array_len; j++) {
                    Object subObj = Array.get(obj, j);
                    if (subObj != null && i > 0){ sb.append(','); }
                    if(subObj == null){
                        continue;
                    }else {
                        i++;
                        walkThrough(subObj, level + 1);
                    }
                }
                sb.append(']');
            } else if (obj instanceof Collection) { // JAVA COLLECTION
                sb.append('[');
                Collection c = (Collection) obj;
                Iterator colElements = c.iterator();
                int i = 0;
                while (colElements.hasNext()) {
                    Object subObj = colElements.next();
                    if (subObj != null && i > 0){ sb.append(','); }
                    if(subObj == null){
                        continue;
                    }else {
                        walkThrough(subObj, level + 1);
                        i++;
                    }
                }
                sb.append(']');
            } else if (!isPrimitive(obj) && !(obj instanceof String)) { // ANY NON STRING/PRIMITIVE OBJECT
                List<Field> fields = Stream.concat(Arrays.stream(obj.getClass().getDeclaredFields()),
                        Arrays.stream(obj.getClass().getSuperclass().getDeclaredFields())).collect(Collectors.toList());
                sb.append('{');
                int i = 0;
                for (Field field : fields) {
                    try {
                        field.setAccessible(true);
                        Object subObj = field.get(obj);
                        if (subObj != null && i > 0){ sb.append(','); }
                        if(subObj == null){
                            continue;
                        } else {
                            i++;
                            sb.append(wrapName(field.getName()));
                            walkThrough(subObj, level + 1);
                        }
                    } catch (IllegalAccessException iae) {
                        System.err.println("Failed to serialize " + field.getName() + ": " + iae.getMessage());
                        return;
                    }
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
