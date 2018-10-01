package ru.otus.shw5.testClasses;

/**
 * this object will not get removed from the list (old gen)
 */

public class ImmortalObject {
    Integer i1 = 100;
    Integer i2 = 200;
    String [] strs = new String[100];

    public ImmortalObject(){
        for(String s : strs){
            s = "QWERTY";
        }
    }

}
