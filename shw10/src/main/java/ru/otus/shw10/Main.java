package ru.otus.shw10;

public class Main {

/*
ДЗ-10: myORM
Создайте в базе таблицу с полями:
id bigint(20) NOT NULL auto_increment
name varchar(255)
age int(3)

Создайте абстрактный класс DataSet. Поместите long id в DataSet.
Добавьте класс UserDataSet (с полями, которые соответствуют таблице) унаследуйте его от DataSet.

Напишите Executor, который сохраняет наследников DataSet в базу и читает их из базы по id и классу.

<T extends DataSet> void save(T user){…}
<T extends DataSet> T load(long id, Class<T> clazz){…}
*/

    public static void main(String [] argz){

    }

}
