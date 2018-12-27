package ru.otus.shw13;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SortTest {

    Random r = new Random();

    private final int TEST_DATA_LEN = 50000;

    Comparable[] array = new Integer[TEST_DATA_LEN];
//    Comparable[] array = new String[TEST_DATA_LEN];

    @Before
    public void generateTestData(){
        for(int i=0; i < array.length; i++){
//            array[i] = String.valueOf(r.nextInt(TEST_DATA_LEN));
            array[i] = r.nextInt(TEST_DATA_LEN);
        }
    }

    @Test
    public void test0_non_threaded_sort(){
        long startTime = System.currentTimeMillis();
        Sorting.bubbleSort(array);
        System.out.println("Sort time: " + (System.currentTimeMillis()-startTime) + "ms.");

        for(int i = 4; i < array.length; i++){
            assertTrue(array[i-1].compareTo(array[i]) <= 0);
        }
    }

    @Test
    public void test1_test_merge_algorithm(){

        Comparable [] mergeArray = Sorting.mergeArrays(
                new Comparable[]{1,3,6,9,9,10,13,17},
                new Comparable[]{-1,1,3,6,7,9,12,100});
        Comparable [] mergeArray2 = Sorting.mergeArrays(
                new Comparable[]{0.1f, 1f, 3.3f},
                new Comparable[]{1f, 3.3f, 55f});
        Comparable [] mergeArray3 = Sorting.mergeArrays(
                new Comparable[]{"a", "e"},
                new Comparable[]{"b", "c", "d"});
        for(int i = 1; i < mergeArray3.length; i++){
            assertTrue(mergeArray3[i-1].compareTo(mergeArray3[i]) <= 0);
        }
        for(Object o : mergeArray3){
            System.out.print(o + ", ");
        }
    }

    @Test
    public void test2_split_partition(){
        Integer [] splitArray = new Integer[11];
        for(int i = 0; i < splitArray.length; i++){
            splitArray[i] = r.nextInt(splitArray.length);
        }
        printArray(splitArray);

        final int threadCount = 4;

        printArray(Sorting.splitPartitions(splitArray, threadCount, 0));
        printArray(Sorting.splitPartitions(splitArray, threadCount, 1));
        printArray(Sorting.splitPartitions(splitArray, threadCount, 2));
        printArray(Sorting.splitPartitions(splitArray, threadCount, 3));

    }

    @Test
    public void test3_thread_sort() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Comparable [] sortedArray = Sorting.threadSort(array, Sorting::bubbleSort);
        System.out.println("Sort time: " + (System.currentTimeMillis()-startTime) + "ms.");

        Arrays.sort(array);
        for(int i = 0; i < array.length; i++){
//            System.out.println("compare " + sortedArray[i-1] + " and " + sortedArray[i]);
            assertEquals(sortedArray[i], array[i]);
        }

    }

    private <T> void printArray(T[] array){
        System.out.println(Arrays.stream(array)
                .map(String::valueOf).reduce((i1, i2) -> i1 + "," + i2));
    }

}
