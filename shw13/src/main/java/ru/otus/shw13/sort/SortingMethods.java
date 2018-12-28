package ru.otus.shw13.sort;

public class SortingMethods {

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
}
