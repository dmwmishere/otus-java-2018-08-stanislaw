package ru.otus.shw2;

public class Main {
    public static void main(String ... args){
        Object obj = 1f;
        System.out.println((obj==null?"null":obj.getClass().getName()) + " size = " + SizeOf.sizeof_bits(obj, "") + " byte(s)");
    }
}
