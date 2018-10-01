package ru.otus.shw5.testClasses;

import java.util.Random;

/**
 * this objects occasionally will be removed from the list
 */

public class MidLifeObject {

    Integer [] arr = new Integer[100];

    public MidLifeObject(){
        Random r = new Random();
        for(int i = 0; i < arr.length; i++){
            arr[i] = r.nextInt();
        }
    }

}
