package ru.otus;

/*
ДЗ 05. Тестовый фреймворк на аннотациях
Написать свой тестовый фреймворк. Поддержать аннотации @Test, @Before, @After.
Запускать вызовом статического метода с
1. именем класса с тестами,
2. именем package в котором надо найти и запустить тесты
 */

import ru.otus.annotations.TestCase;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TestFrameworkProcessor {

    public static void runTests(String packageName) throws ClassNotFoundException {
        List<String> classes = getClassesFromPackage(packageName);
        if (classes == null) {
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

    public static void runTests(Class<?> testCaseClass) {


        if (testCaseClass.isAnnotationPresent(TestCase.class)) {

            TestCaseRunner tcr = new TestCaseRunner(testCaseClass);

            tcr.runTest();
            int failed = tcr.getFailed(), passed = tcr.getPassed();
            System.out.printf("==============================================================\n" +
                    "Total tests = %d, \u001B[31mFailed = %d, \u001B[32mPassed = %d\n\u001B[0m" +
                    "==============================================================\n", failed + passed, failed, passed);

        } else {
            System.out.println(testCaseClass.getName() + " is not a test case. skip.");
        }
    }

    private static ArrayList<String> getClassesFromPackage(final String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ArrayList<String> names = new ArrayList<>();
        URL url = classLoader.getResource(packageName.replace(".", "/"));
        if (url == null) {
            System.err.println("No such package " + packageName);
            return null;
        }
        File[] classFiles = new File(url.toString().replace("file:", "")).listFiles();
        if (classFiles != null) {
            for (File actual : classFiles) {
                String clazz = actual.getName();
                clazz = packageName + "." + clazz.substring(0, clazz.lastIndexOf('.'));
                names.add(clazz);
            }
            return names;
        } else return null;
    }
}
