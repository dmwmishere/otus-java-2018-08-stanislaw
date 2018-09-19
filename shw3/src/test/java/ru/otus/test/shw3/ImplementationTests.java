package ru.otus.test.shw3;

import junit.framework.TestCase;
import org.junit.Test;
import ru.otus.shw3.MyArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImplementationTests extends TestCase {

    List<Integer> lst1 = new ArrayList<>();
    List<Integer> lst2 = new MyArrayList<>();

    int [] testData = new int[]{4,-1,2,6,1,1,78,100,100,2,-1};

    private <T> void compareElementByElement(List<T> l1, List<T> l2){
        assertEquals("L1[" + l1.size() + "] <> L2[" + l2.size() + "]", l1.size(), l2.size());
        for(int i = 0; i < l2.size(); i++){
            assertEquals(l1.get(i), l2.get(i));
        }
    }

    public ImplementationTests(){

        assert lst1.isEmpty() == lst2.isEmpty();

        for(int i: testData){
            lst1.add(i);
            lst2.add(i);
        }

        System.out.println("Test data ArrayList = " + lst1.toString());
        System.out.println("Test data MyArrayList = " + lst2.toString());

    }

    @Test
    public void test1_size(){
        System.out.println("Size = " + lst2.size());
        assertEquals(lst1.size(), lst2.size());
    }

    @Test
    public void test2_access(){
        for(int i = 0; i < lst2.size(); i++){
            assertEquals(lst1.get(i), lst2.get(i));
        }
    }


    @Test
    public void test3_putAtRandom(){
        lst1.add(3, 123);
        lst2.add(3, 123);
        System.out.println("ArrayList   = " + lst1.toString());
        System.out.println("MyArrayList = " + lst2.toString());
        compareElementByElement(lst1, lst2);
    }

    @Test
    public void test4_1_appendCollection(){
        lst1.addAll(Arrays.asList(0,0,0));
        lst2.addAll(Arrays.asList(0,0,0));
        System.out.println("ArrayList   = " + lst1.toString());
        System.out.println("MyArrayList = " + lst2.toString());
        compareElementByElement(lst1, lst2);
    }


    @Test
    public void test4_2_putCollectionAtRandom(){
        lst1.addAll(2, Arrays.asList(0,0,0));
        lst2.addAll(2, Arrays.asList(0,0,0));
        System.out.println("ArrayList   = " + lst1.toString());
        System.out.println("MyArrayList = " + lst2.toString());
        compareElementByElement(lst1, lst2);
    }

    @Test
    public void test5_findItemPosition(){
        System.out.println(lst2.indexOf(2));
        System.out.println(lst2.indexOf(123123));
        assertEquals(lst1.indexOf(2), lst2.indexOf(2));
        assertEquals(lst1.indexOf(123123), lst2.indexOf(123123));


        System.out.println(lst2.lastIndexOf(2));
        System.out.println(lst2.lastIndexOf(123123));
        assertEquals(lst1.lastIndexOf(2), lst2.lastIndexOf(2));
        assertEquals(lst1.lastIndexOf(123123), lst2.lastIndexOf(123123));

    }

    @Test
    public void test6_contains(){
        assertEquals(lst1.contains(1), lst2.contains(1));
        assertEquals(lst1.contains(1231231), lst2.contains(123123));
        List<Integer> subset = Arrays.asList(2,6,1);
        List<Integer> intersection = Arrays.asList(2,5,1);
        assertEquals(lst1.containsAll(subset), lst2.containsAll(subset));
        assertEquals(lst1.containsAll(intersection), lst2.containsAll(intersection));

    }

    @Test
    public void test7_toArray(){
        Object [] a1 = lst1.toArray();
        Object [] a2 = lst2.toArray();
        compareElementByElement(Arrays.asList(a1), Arrays.asList(a2));
        compareElementByElement(Arrays.asList(a2), Arrays.asList(a1));
        Object [] aa1 = new Object[10];
        aa1 = lst1.toArray(aa1);
        Object [] aa2 = new Object[10];
        aa2 = lst2.toArray(aa2);
        compareElementByElement(Arrays.asList(aa1), Arrays.asList(aa2));
        compareElementByElement(Arrays.asList(aa2), Arrays.asList(aa1));

        Object [] bb1 = new Object[1];
        bb1 = lst1.toArray(bb1);
        Object [] bb2 = new Object[1];
        bb2 = lst2.toArray(bb2);
        compareElementByElement(Arrays.asList(bb1), Arrays.asList(bb2));
        compareElementByElement(Arrays.asList(bb2), Arrays.asList(bb1));

    }

    @Test
    public void test8_valueRemoval(){

        System.out.println("ArrayList   = " + lst1.toString());
        System.out.println("MyArrayList = " + lst2.toString());
        int original1 = lst1.remove(2);
        int original2 = lst2.remove(2);
        System.out.println("Remove 2 ArrayList   = " + lst1.toString());
        System.out.println("Remove 2 MyArrayList = " + lst2.toString());
        assertEquals(original1, original2);
        compareElementByElement(lst1, lst2);

        lst1.remove(new Integer(-1));
        lst2.remove(new Integer(-1));

        System.out.println("remove not existing ArrayList   = " + lst1.toString());
        System.out.println("remove not existing MyArrayList = " + lst2.toString());

        List<Integer> intersection = Arrays.asList(2,5,1);
        lst1.removeAll(intersection);
        lst2.removeAll(intersection);

        System.out.println("remove intersection ArrayList   = " + lst1.toString());
        System.out.println("remove intersection MyArrayList = " + lst2.toString());

        compareElementByElement(lst1, lst2);

        List<Integer> retain = Arrays.asList(78,100);

        lst1.retainAll(retain);
        lst2.retainAll(retain);

        System.out.println("retain ArrayList   = " + lst1.toString());
        System.out.println("retain MyArrayList = " + lst2.toString());

        compareElementByElement(lst1, lst2);

        lst1.clear();
        lst2.clear();

        System.out.println("clear ArrayList   = " + lst1.toString());
        System.out.println("clear MyArrayList = " + lst2.toString());

        compareElementByElement(lst1, lst2);
    }

    @Test
    public void test9_setRandomValue() {
        int oldValue1 = lst1.set(2, 1337);
        int oldValue2 = lst2.set(2, 1337);
        System.out.println("set ArrayList   = " + lst1.toString());
        System.out.println("set MyArrayList = " + lst2.toString());
        assertEquals(oldValue1, oldValue2);
        compareElementByElement(lst1, lst2);
    }

    @Test
    public void test10_workWithNulls(){
        MyArrayList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(null);
        list.add(null);
        list.add(null);
        list.addAll(3, Arrays.asList(null, null, null));
        list.set(1, 2);
        list.add(3,6);
        list.addAll(6, Arrays.asList(4,5,6));

        System.out.println("Get null item = " + list.get(2));

        assertTrue(list.contains(null));

        System.out.println("null MyArrayList = " + list.toString());

        while(list.contains(null)) list.remove(null);

        System.out.println("after remove MyArrayList = " + list.toString());
    }

}
