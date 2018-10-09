package ru.otus.shw6;

import ru.otus.data.TableData;

public class Main {

    /*
    ДЗ-06: my cache engine
    Напишите свой cache engine с soft references.
    Добавьте кэширование в DBService из заданий myORM или Hibernate ORM

    VM args:
    -javaagent:../shw5/target/s-hw5-1.0-SNAPSHOT.jar -Xmx256m -Xms256m
     */

    private static final int SIZE = 250;

    public static void main(String [] args){

        DBCachedService db = new DBCachedService();

        for(int i = 0; i < SIZE; i++){
            db.getById(i);
//            db.insertInto(new TableData(i, "User input", new byte [1024*1024]));
            // Uncomment if using with javaagent:
            // Thread.sleep(1000);
        }

        db.printCacheStats();

        for(int i = SIZE-1; i >= 0; i--){
            System.out.println(db.getById(i).toString());
        }

        db.printCacheStats();

    }
}
