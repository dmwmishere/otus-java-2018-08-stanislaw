package ru.otus.tests;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.annotations.TestCase;

import java.util.HashSet;
import java.util.Set;

@TestCase(caseName = "#1")
public class TestCase1 {

    Set<Integer> testData = new HashSet<>();

    @Before
    public void beforeTest1(){
        System.out.println("1. Before test preparation 1");
        testData.add(1);
    }

    @Before
    public void beforeTest2(){
        System.out.println("1. Before test preparation 2");
        testData.add(2);
    }

    @Test(order = 2)
    public void step3_1(){
        System.out.println("1. Step 3.1");
        if(!testData.contains(1)) throw new IllegalStateException("Item 1 not found in test data!");
    }

    @Test(order = 2)
    public void step3_2(){
        System.out.println("1. Step 3.2");
        if(!testData.contains(2)) throw new IllegalStateException("Item 2 not found in test data!");
    }

    @Test(order = 0)
    public void step1_1(){
        System.out.println("1. Step 1.1");
        if(!testData.contains(1)) throw new IllegalStateException("Item 1 not found in test data!");
    }

    @Test(order = 0)
    public void step1_2(){
        System.out.println("1. Step 1.2");
        if(!testData.contains(2)) throw new IllegalStateException("Item 2 not found in test data!");
    }

    @Test(order = 1)
    public void step2(){
        System.out.println("1. Step 2");
        if(!testData.contains(3)) throw new IllegalStateException("Item 3 not found in test data!");
    }

    @After
    public void afterTest1(){
        System.out.println("1. After test 1");
    }

    @After
    public void afterTest2(){
        System.out.println("1. After test 2");
    }
}
