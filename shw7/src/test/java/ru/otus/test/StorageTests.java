package ru.otus.test;

import org.junit.Before;
import org.junit.Test;
import ru.otus.shw7.currency.Bill;
import ru.otus.shw7.currency.CurrencyCode;
import ru.otus.shw7.currency.Storage;

import static org.junit.Assert.assertEquals;

public class StorageTests {

    private Storage rubStorage;

    @Before
    public void createRubStorage(){

        System.out.println("init storage...");

        rubStorage = new Storage(CurrencyCode.RUB);


        rubStorage.deposit(new Bill(CurrencyCode.RUB, 50));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 50));

        rubStorage.deposit(new Bill(CurrencyCode.RUB, 100));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 100));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 100));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 100));

        rubStorage.deposit(new Bill(CurrencyCode.RUB, 200));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 200));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 200));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 200));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 200));

        rubStorage.deposit(new Bill(CurrencyCode.RUB, 1000));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 1000));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 1000));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 1000));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 1000));

        rubStorage.deposit(new Bill(CurrencyCode.RUB, 5000));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 5000));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 5000));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 5000));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 5000));

    }

    @Test(expected = IllegalStateException.class)
    public void test0_billCheck(){
        new Bill(CurrencyCode.EUR, 100);
        new Bill(CurrencyCode.RUB, 500);
        new Bill(CurrencyCode.USD, 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test1_1_depositInvalidCurrency(){
        rubStorage.deposit(new Bill(CurrencyCode.EUR, 200));
    }

    @Test
    public void test1_2_deposit(){
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 200));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 1000));
        rubStorage.deposit(new Bill(CurrencyCode.RUB, 5000));

        assertEquals((long)rubStorage.printStorageReport().get(5000), 6L);
        assertEquals((long)rubStorage.printStorageReport().get(1000), 6L);
        assertEquals((long)rubStorage.printStorageReport().get(200), 6L);

    }

    @Test
    public void test0_withdrawRC(){
        assertEquals(rubStorage.withdraw(new Bill(CurrencyCode.RUB, 50)), 0);
        assertEquals(rubStorage.withdraw(new Bill(CurrencyCode.RUB, 50)), 0);
        assertEquals(rubStorage.withdraw(new Bill(CurrencyCode.RUB, 50)), 3);
        assertEquals(rubStorage.withdraw(new Bill(CurrencyCode.RUB, 50)), 3);
        assertEquals(rubStorage.withdraw(new Bill(CurrencyCode.RUB, 100)), 0);
        assertEquals(rubStorage.withdraw(new Bill(CurrencyCode.RUB, 2000)), 2);
        rubStorage.printStorageReport();
    }
}
