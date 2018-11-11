package ru.otus.test.shw9;

import com.google.gson.Gson;
import org.junit.Test;
import ru.otus.shw9.ClassWalkThrough;
import ru.otus.shw9.JsonVisitor;
import ru.otus.test.shw9.data.TestClass1;
import ru.otus.test.shw9.data.TestClass2;
import ru.otus.test.shw9.data.TestClass3;
import ru.otus.test.shw9.data.TestClass4;

import java.util.Arrays;

public class JsonTests {

//    @Test
//    public void test0_TEST_DESC(){
//        Gson gson = new Gson();
//        System.out.println(gson.toJson(new TestClass1()));
//        System.out.println(gson.toJson(new TestClass2()));
//        System.out.println(gson.toJson(new TestClass3()));
//        System.out.println(gson.toJson(Arrays.asList(new TestClass1(),new TestClass1(),new TestClass1())));
//    }

    @Test
    public void test1_TestClass1(){
        ClassWalkThrough classWalkThrough = new ClassWalkThrough(new JsonVisitor());
        classWalkThrough.walkThrough(new TestClass1(), 0);
    }

    @Test
    public void test2_TestClass2(){
        ClassWalkThrough classWalkThrough = new ClassWalkThrough(new JsonVisitor());
        classWalkThrough.walkThrough(new TestClass2(), 0);
    }

    @Test
    public void test3_TestClass3(){
        ClassWalkThrough classWalkThrough = new ClassWalkThrough(new JsonVisitor());
        classWalkThrough.walkThrough(new TestClass3(), 0);
    }

    @Test
    public void test4_TestClass4(){
        ClassWalkThrough classWalkThrough = new ClassWalkThrough(new JsonVisitor());
        classWalkThrough.walkThrough(new TestClass4(), 0);
    }
}
