package ru.otus;

public class Main {
    public static void main(String []argz) throws Exception{
        TestFrameworkProcessor.runTests(Class.forName("ru.otus.tests.TestCase2"));
        TestFrameworkProcessor.runTests("ru.otus.tests");
    }
}
