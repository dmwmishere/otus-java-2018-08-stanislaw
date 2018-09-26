package ru.otus.tests;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.annotations.TestCase;

import java.util.HashSet;
import java.util.Set;

@TestCase(caseName = "#4")
public class TestCase4 {

    Set<Integer> i = new HashSet<>();

    @Before
    public void init_i(){
        if(i.contains(1)) throw new IllegalStateException("There is data from another test step");
        i.add(2);
        System.out.println("i before = " + i.toString());
    }

    @Test(order=2)
    public void test_step_1(){
        if(i.contains(1)) throw new IllegalStateException("There is data from another test step");
        i.remove(2);
    }

    @Test(order=1)
    public void test_step_2(){
        if(i.contains(1)) throw new IllegalStateException("There is data from another test step");
        i.add(1);
    }

    @After
    public void after(){
        i.remove(3);
        System.out.println("i after = " + i.toString());
    }
}
