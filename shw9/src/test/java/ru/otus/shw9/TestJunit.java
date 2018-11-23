package ru.otus.shw9;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import ru.otus.shw9.data.TestClass1;
import ru.otus.shw9.data.TestClass2;
import ru.otus.shw9.data.TestClass4;
import ru.otus.shw9.data.TestClass3;

import java.util.*;

public class TestJunit {

    Gson gson = null;
    ClassWalkThrough classWalkThrough = null;

    @Before
    public void initSerizlizers(){
        gson = new Gson();
        classWalkThrough = new ClassWalkThrough();
    }

    @Test
    public void test1_TestClass1(){
        String json = classWalkThrough.toJson(new TestClass1());
        System.out.println(json);
        System.out.println(gson.toJson(new TestClass1()));
        assert json.contains("\"a\":0");
        assert !json.contains(",,");
    }

    @Test
    public void test2_TestClass2(){
        String json = classWalkThrough.toJson(new TestClass2());
        System.out.println(json);
        System.out.println(gson.toJson(new TestClass2()));
        assert json.matches(".*\\[(\\d,?){3}\\].*");
    }

    @Test
    public void test3_TestClass3(){
        String json = classWalkThrough.toJson(new TestClass3());
        System.out.println(json);
        System.out.println(gson.toJson(new TestClass3()));
        assert json.matches(".*\\[(\\d,?){3}\\].*");
        assert json.contains("\"a\":0");
        assert !json.contains(",,");
    }

    @Test
    public void test4_TestClass4(){
        String json = classWalkThrough.toJson(new TestClass4());
        System.out.println(json);
        System.out.println(gson.toJson(new TestClass4()));
        assert json.contains("\"a\":0");
        assert !json.contains(",,");
    }

    @Test
    public void test5_null(){
        String json = classWalkThrough.toJson(null);
        System.out.println(json);
        assert json == null;
    }

    @Test
    public void test6_primitive_array(){
        String json = classWalkThrough.toJson(new int []{1,2,3,4,5,6});
        System.out.println(json);
        assert json.matches(".*\\[(\\d,?)+\\].*");
    }

    @Test
    public void test7_collection(){
        Set<String> strs = new HashSet<>();
        strs.add("QWE");
        strs.add("RTY");
        strs.add("TEST");
        String json = classWalkThrough.toJson(strs);
        System.out.println(json);
        assert json.matches(".*\\[(\"\\w+\",?)+\\].*");
    }

    @Test
    public void test8_complex_collection(){
        System.out.println(classWalkThrough.toJson(
                Arrays.asList(
                        null,
                        new TestClass1(),
                        new TestClass2(),
                        new TestClass3(),
                        null,
                        null,
                        new TestClass4(),
                        null
                )
        ));
    }

    @Test
    public void test9_complex_array(){
        System.out.println(classWalkThrough.toJson(
                new Object []{
                        null,
                        new TestClass1(),
                        new TestClass2(),
                        new TestClass3(),
                        null,
                        null,
                        new TestClass4(),
                        null
                }
        ));
    }
}
