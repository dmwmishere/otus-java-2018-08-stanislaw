package ru.otus.shw13;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

public class Sorting {

    static class ThreadedSort<T> extends Thread{

        private Consumer<T> sortMethod;

        private Set<Thread> threadsPool;

        private T array;

        public ThreadedSort(T array, Set<Thread> threadsPool, Consumer<T> sortMethod){
            this.sortMethod = sortMethod;
            this.threadsPool = threadsPool;
            this.array = array;
            threadsPool.add(this);

//            start();
        }

        @Override
        public void run(){
            sortMethod.accept(array);
            threadsPool.remove(this);
        }

    }

    public static <T extends Comparable> void bubbleSort(T[] array) {
        for (int i = 0; i < array.length; i++)
            for (int j = i; j < array.length; j++)
                compareAndSwap(array, i, j);
    }

    private static <T extends Comparable> void compareAndSwap(T[] array, int i, int j) {
        if (array[i].compareTo(array[j]) > 0) {
            T t = array[i];
            array[i] = array[j];
            array[j] = t;
        }
    }

    public static <T extends Comparable> T[] threadSort(T[] array, Consumer<T[]> sortMethod) throws InterruptedException {
        final int threadCount = 4;
        Stack<T[]> arrays = new Stack<>();
        for(int i = 0; i < threadCount; i++){
            arrays.push(splitPartitions(array, threadCount, i));
        }
        Set<Thread> threads = new HashSet<>();

        ExecutorService executor = Executors.newFixedThreadPool(4);

        arrays.forEach(subArray -> executor.execute(new ThreadedSort<>(subArray, threads, sortMethod)));

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        while(arrays.size() > 1){
            arrays.push(mergeArrays(arrays.pop(), arrays.pop()));
        }

        return arrays.pop();

    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable> T[] mergeArrays(T[] array1, T[] array2){
        T[] concatArray = (T[])Array.newInstance(array1[0].getClass(),
                array1.length+array2.length);
        int i = 0, // array1 counter
            j = 0, // array2 counter
            k = 0; // concatArray counter
        while(k < concatArray.length){
            if(i < array1.length && j < array2.length) {
                concatArray[k++] = array1[i].compareTo(array2[j]) < 0 ? array1[i++] : array2[j++];
            } else {
                concatArray[k++] = i < array1.length ? array1[i++] : array2[j++];
            }
        }
        return concatArray;
    }

    public static <T extends Comparable> T[] splitPartitions(T[] array, final int threadCount, final int idx) {
        int parts = Math.round(array.length/threadCount);
        int start = parts*idx;
        int stop = start + parts;
        if(idx == threadCount-1){
            stop += array.length-stop;
        }
//        System.out.println(String.format("Array len = %d, Parts = %d, start = %d, stop = %d",
//                array.length, parts, start, stop));
        T[] subArray = (T[])Array.newInstance(array[0].getClass(), stop-start);
        System.arraycopy(array,start,subArray, 0,stop-start);
        return subArray;
    }



}
