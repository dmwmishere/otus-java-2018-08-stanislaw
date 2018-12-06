package ru.otus.shw10.reflection;

import ru.otus.shw6.engine.CacheEngine;

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
        System.out.println("GET FIELDS FOR " + obj.getClass().getName());
        List<String> fieldList = new ArrayList<>();
        List<Field> fields = Stream.concat(Arrays.stream(obj.getClass().getDeclaredFields()),
                Arrays.stream(obj.getClass().getSuperclass().getDeclaredFields())).collect(Collectors.toList());
        for (Field field : fields) {
            if (field.getType().isPrimitive() || field.getType().isAssignableFrom(String.class)) {
                field.setAccessible(true);
                fieldList.add(field.getName());
            } else {
                System.out.println(field.getName() + ": type of '" + field.getType().getSimpleName() + "' not supported");
            }
        }
        System.out.println(obj.getClass().getSimpleName() + " fields = " + fieldList);
        return fieldList;
    }

    public static List<String> getFieldsList(Object obj, CacheEngine<String, List<String>> cache) {
        List<String> fieldList = cache.get(obj.getClass().getName());
        if (fieldList == null) {
            System.out.println("Class fields not in cache");
            fieldList = getFieldsList(obj);
//            cache.put(obj.getClass().getName(), fieldList);
        } else {
            System.out.println("Got fields from cache");
        }
        return fieldList;
    }

}
