package ru.otus.tests;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.annotations.TestCase;

public class TestCase3 {

    public TestCase3(){
        System.out.println("Will not execute this case as long as there no TestCase annotation");
    }

    @Before
    public void beforeTest(){
        System.out.println("3. Before test");
    }

    @Test
    public void step1(){
        System.out.println("3. Step 1");
    }

    @Test
    public void step2(){
        System.out.println("3. Step 2");
    }

    @After
    public void afterTest(){
        System.out.println("3. After test");
    }
}
