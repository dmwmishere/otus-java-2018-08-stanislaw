package ru.otus.test.shw9;

import com.google.gson.Gson;
import org.junit.Test;
import ru.otus.shw9.ClassWalkThrough;
import ru.otus.test.shw9.data.TestClass1;
import ru.otus.test.shw9.data.TestClass2;
import ru.otus.test.shw9.data.TestClass3;
import ru.otus.test.shw9.data.TestClass4;

import java.util.*;

public class JsonTests {

    @Test
    public void test0_TEST_DESC(){
        Gson gson = new Gson();
        System.out.println(gson.toJson(new TestClass1()));
        System.out.println(gson.toJson(new TestClass2()));
        System.out.println(gson.toJson(new TestClass3()));
        System.out.println(gson.toJson(Arrays.asList(new TestClass1(),new TestClass1(),new TestClass1())));
        System.out.println(gson.toJson(new TestClass4()));
        System.out.println(gson.toJson(null));
    }

    @Test
    public void test1_TestClass1(){
        ClassWalkThrough classWalkThrough = new ClassWalkThrough();
        System.out.println(classWalkThrough.toJson(new TestClass1()));
    }

    @Test
    public void test2_TestClass2(){
        ClassWalkThrough classWalkThrough = new ClassWalkThrough();
        System.out.println(classWalkThrough.toJson(new TestClass2()));
    }

    @Test
    public void test3_TestClass3(){
        ClassWalkThrough classWalkThrough = new ClassWalkThrough();
        System.out.println(classWalkThrough.toJson(new TestClass3()));
    }

    @Test
    public void test4_TestClass4(){
        ClassWalkThrough classWalkThrough = new ClassWalkThrough();
        System.out.println(classWalkThrough.toJson(new TestClass4()));
    }

    @Test
    public void test5_null(){
        ClassWalkThrough classWalkThrough = new ClassWalkThrough();
        System.out.println(classWalkThrough.toJson(null));
    }

    @Test
    public void test6_primitive_array(){
        ClassWalkThrough classWalkThrough = new ClassWalkThrough();
        System.out.println(classWalkThrough.toJson(new int []{1,2,3,4,5,6}));
    }

    @Test
    public void test7_collection(){
        Set<String> strs = new HashSet<>();
        strs.add("QWE");
        strs.add("RTY");
        strs.add("TEST");
        ClassWalkThrough classWalkThrough = new ClassWalkThrough();
        System.out.println(classWalkThrough.toJson(strs));
    }

    @Test
    public void test8_complex_collection(){
        ClassWalkThrough classWalkThrough = new ClassWalkThrough();
        System.out.println(classWalkThrough.toJson(
                Arrays.asList(
                        new TestClass1(),
                        new TestClass2(),
                        new TestClass3(),
                        new TestClass4()
                )
        ));
    }

}
