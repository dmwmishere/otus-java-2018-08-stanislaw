package ru.otus.shw13.utils;

import java.lang.reflect.Array;

public class ArrayHelper {

    @SuppressWarnings("unchecked")
    public static <T extends Comparable> T[] mergeArrays(T[] array1, T[] array2){
        T[] concatArray = (T[]) Array.newInstance(array1[0].getClass(),
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
