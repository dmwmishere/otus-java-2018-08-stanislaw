package ru.otus.shw9;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class ClassWalkThrough {

    private final Visitor visitor;

    public ClassWalkThrough(Visitor visitor) {
        this.visitor = visitor;
    }

    public static boolean isPrimitive(Object o) {
        return o instanceof Long || o instanceof Integer || o instanceof Float || o instanceof Boolean
                || o instanceof Short || o instanceof Byte || o instanceof Character || o instanceof Double;
    }

    public void walkThrough(Object obj, int level) {
        if (obj == null) {
            return;
        } else {
            if(!isPrimitive(obj)){
                Class<?> cls = obj.getClass();
                if (cls.isArray()) { // arrays
                    System.out.println("ARRAY");
                    int array_len = Array.getLength(obj);
                    for (int i = 0; i < array_len; i++) {
                        walkThrough(Array.get(obj, i), level);
                    }
                } else { // other types
                    for (Field field : cls.getDeclaredFields()) {
                        System.out.println("CLASS");
                        field.setAccessible(true);
                        try {
                            Object subObj = field.get(obj);
                            visitor.visit(field.getName(), subObj, level);
                            if(!isPrimitive(subObj)) walkThrough(subObj, level + 1);
                        } catch (IllegalAccessException iae) {
                            System.err.println("Failed to access field " + field.getName() + ": " + iae.getMessage());
                        }
                    }
                }
            } else {
                visitor.visit(null, obj, level);
            }
        }
    }

}
