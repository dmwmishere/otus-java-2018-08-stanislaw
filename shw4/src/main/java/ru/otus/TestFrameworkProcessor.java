package ru.otus;

/*
ДЗ 05. Тестовый фреймворк на аннотациях
Написать свой тестовый фреймворк. Поддержать аннотации @Test, @Before, @After.
Запускать вызовом статического метода с
1. именем класса с тестами,
2. именем package в котором надо найти и запустить тесты
 */

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.annotations.TestCase;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestFrameworkProcessor {

    public static void runTests(String packageName) throws ClassNotFoundException {
        List<String> classes = getClassesFromPackage(packageName);
        if(classes == null){
            System.err.println("NO CLASSES FOUND IN " + packageName);
            return;
        }
        for (String clazz : classes) {
            Class<?> cls = Class.forName(clazz);
            if (cls.isAnnotationPresent(TestCase.class)) {
                System.out.println("Executing test case = " + cls.getAnnotation(TestCase.class).caseName());
                runTests(cls);
            } else {
                System.out.println(cls.getName() + " is not a test case. Skip");
            }
        }
    }

    public static void runTests(Class<?> ... testCaseClasses) {

        int failed = 0, passed = 0, skipped = 0;
        for(Class<?> testCaseClass : testCaseClasses) {
            if (testCaseClass.isAnnotationPresent(TestCase.class)) {

                List<Method> before = methodsByAnnotation(testCaseClass, Before.class);

                List<Method> testStep = methodsByAnnotation(testCaseClass, Test.class);
                testStep.sort((m1, m2) -> { // execute test steps in defined order
                    int order1 = m1.getAnnotation(Test.class).order(),
                            order2 = m2.getAnnotation(Test.class).order();

                    if (order1 == order2) return 0;
                    return order1 < order2 ? -1 : 1;
                });

                List<Method> after = methodsByAnnotation(testCaseClass, After.class);


                try {
                    Object obj = testCaseClass.newInstance();

                    for (Method method : before) {
                        // skip test case execution if there are any errors before test steps
                        if (!executeTest(method, obj)) return;
                    }

                    for (Method method : testStep) {
                        if (failed > 0) { // skip all steps after any failures occurred
                            skipped++;
                        } else {
                            if (executeTest(method, obj)) passed++;
                            else failed++;
                        }
                    }

                    for (Method method : after) {
                        executeTest(method, obj);
                    }

                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                    return;
                }

                System.out.printf("==============================================================\n" +
                        "Total tests = %d, \u001B[31mFailed = %d, \u001B[32mPassed = %d, \u001B[33mskipped = %d\n\u001B[0m" +
                        "==============================================================\n", failed + passed + skipped, failed, passed, skipped);

            } else {
                System.out.println(testCaseClass.getName() + " is not a test case. skip.");
            }
        }
    }

    private static boolean executeTest(Method method, Object instance) throws IllegalAccessException {
        try {
            method.invoke(instance);
            return true;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.err.printf("test step %s failed with error: %s\n", method.getName(), e.getCause());
            return false;
        }
    }

    private static List<Method> methodsByAnnotation(Class<?> cls, Class<? extends Annotation> annotation) {
        return Stream.of(cls.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(annotation)).collect(Collectors.toList());
    }

    private static ArrayList<String> getClassesFromPackage(final String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ArrayList<String> names = new ArrayList<>();
        URL url = classLoader.getResource(packageName.replace(".", "/"));
        if(url == null){
            System.err.println("No such package " + packageName);
            return null;
        }
        File[] classFiles = new File(url.toString().replace("file:", "")).listFiles();
        if(classFiles != null) {
            for (File actual : classFiles) {
                String clazz = actual.getName();
                clazz = packageName + "." + clazz.substring(0, clazz.lastIndexOf('.'));
                names.add(clazz);
            }
            return names;
        } else return null;
    }
}
