package ru.otus.test.shw3;

import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.otus.shw3.MyArrayList;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.*;


public class ColleciontsTests extends TestCase {

    MyArrayList<Float> lst2 = new MyArrayList<>();

    List<Float> testData = Arrays.asList(1f, 3.14f, 2.71f, 5f, -1.5f, 0f, -441f);


    private static void printArray(String pref, List lst) {
        System.out.printf("%-10s = %s\n", pref, lst.stream().reduce((ss1, ss2) -> String.format("%5s,%5s", ss1, ss2)).toString());
    }

    public ColleciontsTests(){
        lst2.addAll(testData);
        printArray("Initial", lst2);
        System.out.println();
    }

    @Test
    public void test1_shuffle(){
        shuffle(lst2);
        printArray("Shuffle", lst2);

        int i = 0;
        for(float f: lst2) if(f != testData.get(i++)) break;
        assertFalse(i == lst2.size());

        shuffle(lst2);
        printArray("Shuffle", lst2);

    }

    @Test
    public void test2_reverse(){
        reverse(lst2);

        int i = lst2.size()-1;
        for(float f: lst2) assertEquals(testData.get(i--), f);

        printArray("Reverse", lst2);
    }

    @Test
    public void test3_rotate(){
        rotate(lst2, lst2.size() / 2);
        printArray("Rorate", lst2);

        assertEquals(testData.get(0), lst2.get(lst2.size()/2));
    }

    @Test
    public void test4_sort_min_max(){

        float minVal = min(lst2);
        float maxVal = max(lst2);

        System.out.printf("Min = %f, max = %f\n", minVal, maxVal);

        sort(lst2);
        printArray("Sorted", lst2);

        assertEquals(minVal, lst2.get(0));

        assertEquals(maxVal, lst2.get(lst2.size()-1));

    }

    @Test
    public void test5_addAll(){
        addAll(lst2, 100.1f, 100.2f, 100.3f);

        printArray("AddAll", lst2);

        assertTrue(lst2.containsAll(Arrays.asList(100.1f, 100.2f, 100.3f)));

    }

    @Test
    public void test6_fill(){
        int i = lst2.size();
        fill(lst2, -100f);
        printArray("Fill", lst2);

        assertEquals(lst2.size(), i);

        for(float f : lst2) assertEquals(-100f, f);

    }

    @Test
    public void test7_toUnmodifiable(){
        try {
            unmodifiableList(lst2).add(1f);
        } catch(UnsupportedOperationException uop){
            System.out.println("add not implemented!");
        } catch(Exception e){
            fail("the collection was modified!");
        }
    }

}
