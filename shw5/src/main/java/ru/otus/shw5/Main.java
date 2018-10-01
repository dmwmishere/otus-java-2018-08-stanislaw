package ru.otus.shw5;

import ru.otus.shw5.testClasses.ImmortalObject;
import ru.otus.shw5.testClasses.MidLifeObject;
import ru.otus.shw5.testClasses.ShortLifeObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ДЗ 04. Измерение активности GC
 * Написать приложение, которое следит за сборками мусора и пишет в лог количество сборок каждого типа (young, old) и время которое ушло на сборки в минуту.
 * Добиться OutOfMemory в этом приложении через медленное подтекание по памяти (например добавлять элементы в List и удалять только половину).
 * Настроить приложение (можно добавлять Thread.sleep(...)) так чтобы оно падало с OOM примерно через 5 минут после начала работы.
 * Собрать статистику (количество сборок, время на сборрки) по разным типам GC.
 */

@SuppressWarnings("unused")
public class Main {
    public static void main(String [] args) {
        List<Object> lst = new ArrayList<>();
        long l = 1;
        while(true){

            long startTime = System.currentTimeMillis();
            try {

                // Change case number below:
                testCase2(lst, ++l);


            } catch(Exception e){
                System.err.println("Occured exception while test: " + e.getMessage());
                break;
            }
//            System.out.println("Objects added in " + (System.currentTimeMillis() - startTime) + " ms. " +
//                    "Pool stats = " + lst.stream().collect(Collectors.groupingBy(o -> o.getClass().getSimpleName(), Collectors.counting())) +
//                    ", List size = " + lst.size());
        }
    }

    /*
    Кейс 1.1: создаются об-ты Immortal (30%) и ShortLive (70%). Каждый 10й цикл ShortLive удаляются из списка.
    */
    private static void testCase1(List<Object> lst, final long cycle) throws Exception{
        Random r = new Random();
        for(int i = 0; i < 300; i++){
            lst.add(r.nextInt(100) > 70 ? new ImmortalObject() : new ShortLifeObject());
        }

        Thread.sleep(500);
        if(cycle % 10 == 0)
            lst.removeIf(o -> o.getClass().getName().endsWith("ShortLifeObject"));
    }

    /*
    Кейс 2.1: создаются об-ты MidLife (30%) и ShortLife (70%). Каждый 50й цикл MidLife удаляются из списка.
     */
    private static void testCase2(List<Object> lst, final long cycle) throws Exception{
        Random r = new Random();
        for(int i = 0; i < 2000; i++){
            lst.add(r.nextInt(100) > 70 ? new MidLifeObject() : new ShortLifeObject());
        }

        Thread.sleep(500);
        if(cycle % 10 == 0)
            lst.removeIf(o -> o.getClass().getName().endsWith("ShortLifeObject"));
        if(cycle % 50 == 0)
            System.out.println("Remove MidLifeObject");
            lst.removeIf(o -> o.getClass().getName().endsWith("MidLifeObject"));
    }

}
