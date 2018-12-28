package ru.otus.shw13.sort;

import ru.otus.shw13.utils.ArrayHelper;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class SortingUtils {

    public static <T extends Comparable> T[] threadSort(T[] array, Consumer<T[]> sortMethod, final int threadCount) throws InterruptedException {
        Stack<T[]> arrays = new Stack<>();
        for(int i = 0; i < threadCount; i++){
            arrays.push(ArrayHelper.splitPartitions(array, threadCount, i));
        }
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        arrays.forEach(subArray -> executor.execute(() -> sortMethod.accept(subArray)));

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        while(arrays.size() > 1){
            arrays.push(ArrayHelper.mergeArrays(arrays.pop(), arrays.pop()));
        }

        return arrays.pop();

    }


}
