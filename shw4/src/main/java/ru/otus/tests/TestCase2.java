package ru.otus.tests;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.annotations.TestCase;

@TestCase(caseName = "#2")
public class TestCase2 {

    public TestCase2(){
        System.out.println("Executing test case #2");
    }

    @Before
    public void beforeTest(){
        System.out.println("2. Before test");
    }

    @Test
    public void step1(){
        System.out.println("2. Step 1");
    }

    @Test
    public void step2(){
        System.out.println("2. Step 2");
    }

    @After
    public void afterTest(){
        System.out.println("2. After test");
    }
}
