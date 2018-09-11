package ru.otus.test;

import org.junit.Test;
import ru.otus.shw2.SizeOf;
import ru.otus.test.data.TestClass1;
import ru.otus.test.data.TestClass2;
import ru.otus.test.data.TestClass3;
import ru.otus.test.data.TestClass4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class TestObjectSize {

//    ДЗ 02. Измерение памяти
//    Написать стенд для определения размера объекта. Определить размер пустой строки и пустых контейнеров. Определить рост размера контейнера от количества элементов в нем.
//            Например:
//    Object — 8 bytes,
//    Empty String — 40 bytes
//    Array — from 12 bytes

    @Test
    public void test1_PrimitiveSize(){
        for(Object o : new Object [] {
                false, 1, 3.14f, 1.0, 1L, 'c', (short) 5, (byte) 255}){
            SizeOf.sizeof_bits(o, "");
        }
    }

    @Test
    public void test2_ArraySize(){
        for(Object o : new Object []{
            new int[0], new int[]{1,2,3}, new Object[0], new Object[]{1, 1f, 1L}
        }){
            SizeOf.sizeof_bits(o, "");
        }
    }

    @Test
    public void test3_CustomClassSize(){
        for(Object o : new Object []{
                new TestClass1(), new TestClass2(), new TestClass3(), new TestClass4()
        }){
            SizeOf.sizeof_bits(o, "");
        }
    }

    @Test
    public void test4_StringsAndObjectSize(){
        for(Object o : new Object []{
                null, new Object(), "", "sample text"
        }){
            SizeOf.sizeof_bits(o, "");
        }
    }

    @Test
    public void test5_ListContainerSize(){
        for(Object o : new Object []{
                new ArrayList(), Arrays.asList(1,2,3,4,5), Collections.singletonList(0)
        }){
            SizeOf.sizeof_bits(o, "");
        }
    }

    @Test
    public void test6_MapContainerSize(){
        HashMap<String, Integer> dict = new HashMap<>();
        SizeOf.sizeof_bits(dict, "");
        dict.put("A", 1);
        dict.put("B", 2);
        dict.put("C", 3);
        dict.put("D", 4);
        dict.put("E", 5);
        SizeOf.sizeof_bits(dict, "");
    }
}
