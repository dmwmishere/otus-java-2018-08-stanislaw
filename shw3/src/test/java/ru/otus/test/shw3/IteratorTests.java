package ru.otus.test.shw3;

import junit.framework.TestCase;
import org.junit.Test;
import ru.otus.shw3.MyArrayList;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class IteratorTests extends TestCase {

    MyArrayList<Float> lst2 = new MyArrayList<>();

    List<Float> testData = Arrays.asList(1f, 3.14f, 2.71f, 5f, -1.5f, 0f, -441f);

    public IteratorTests(){
        lst2.addAll(testData);
        System.out.println("Test data MyArrayList = " + lst2.toString());
    }

    @Test
    public void test1_iterate_through_list(){

        Iterator<Float> iter = lst2.iterator();
        int i = 0;
        while(iter.hasNext()){
            float value = iter.next();
            assertEquals(testData.get(i++), value);
            System.out.println("Value = " + value);
        }
    }

    @Test
    public void test1_delete_by_iter(){

        Iterator<Float> iter = lst2.iterator();
        while(iter.hasNext()){
            float value = iter.next();
            if(value < 0){
                iter.remove();
            }
            System.out.println("Value = " + value);
        }

        for(float f: lst2) assertTrue(f >= 0);

        System.out.println("REMOVED FORM LIST = " + lst2.toString());

    }

    @Test
    public void test3_list_iterator(){
        Object arr[] = lst2.toArray();
        ListIterator<Float> it = lst2.listIterator();
        for (int i=0; i<arr.length; i++) {
            it.next();
            it.set((float)arr[i]*2f);
        }

        int i = 0;
        for(float f: lst2) assertEquals(testData.get(i++)*2f, f);

        System.out.println("New data through iterator = " + lst2.toString());

    }

}
