package ru.otus.shw10.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectionHelper {
    public static Object reflectionGetter(Object obj, String fieldName)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return obj.getClass().getMethod("get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1))
                .invoke(obj);
    }

    public static void reflectionSetter(Object obj, String fieldName, Object arg)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        obj.getClass().getMethod("set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1), arg.getClass())
                .invoke(obj, arg);
    }

    public static List<String> getFieldsList(Object obj) {
        List<String> fieldList = new ArrayList<>();
        List<Field> fields = Stream.concat(Arrays.stream(obj.getClass().getDeclaredFields()),
                Arrays.stream(obj.getClass().getSuperclass().getDeclaredFields())).collect(Collectors.toList());
        for (Field field : fields) {
            System.out.println("FIELD = " + field.getName());
            field.setAccessible(true);
            fieldList.add(field.getName());
        }
        return fieldList;
    }
}
