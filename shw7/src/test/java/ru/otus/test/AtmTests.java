package ru.otus.test;

import org.junit.Test;
import ru.otus.shw7.atm.RuAtm;
import ru.otus.shw7.atm.UsdAtm;
import ru.otus.shw7.currency.Bill;
import ru.otus.shw7.currency.CurrencyCode;
import ru.otus.shw7.user.Account;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class AtmTests {

    @Test
    public void test0_enterRuAtm(){
        RuAtm atm = new RuAtm();
        Account acc = new Account(1234567890, CurrencyCode.RUB, 5000);
        atm.enterAtm(acc);
        assertEquals(atm.balance(), 5000L);
        atm.printCheck();
        atm.quitAtm();

    }

    @Test
    public void test0_depositAll(){
        RuAtm atm = new RuAtm();
        Account acc = new Account(1234567890, CurrencyCode.RUB, 5000);
        atm.enterAtm(acc);

        List<Bill> bills = new ArrayList<>();

        bills.add(new Bill(CurrencyCode.RUB, 50));
        bills.add(new Bill(CurrencyCode.RUB, 50));
        bills.add(new Bill(CurrencyCode.RUB, 50));
        bills.add(new Bill(CurrencyCode.RUB, 50));
        bills.add(new Bill(CurrencyCode.RUB, 100));
        bills.add(new Bill(CurrencyCode.RUB, 100));
        bills.add(new Bill(CurrencyCode.RUB, 2000));

        atm.deposit(bills);

        atm.atmStorageReport();

        assertEquals(7400, acc.getBalance());

    }

    @Test (expected = IllegalArgumentException.class)
    public void test1_depositInvalid(){
        RuAtm atm = new RuAtm();
        Account acc = new Account(1234567890, CurrencyCode.RUB, 5000);
        atm.enterAtm(acc);

        List<Bill> bills = new ArrayList<>();

        bills.add(new Bill(CurrencyCode.RUB, 50));
        bills.add(new Bill(CurrencyCode.USD, 5));

        atm.deposit(bills);

    }

    @Test
    public void test2_depositZero(){
        RuAtm atm = new RuAtm();
        Account acc = new Account(1234567890, CurrencyCode.RUB, 5000);
        atm.enterAtm(acc);

        List<Bill> bills = new ArrayList<>();

        atm.deposit(bills);

        atm.atmStorageReport();

        assertEquals(5000, acc.getBalance());
    }


    @Test
    public void test3_withdrawRUB(){

        for(int sum2withdraw : Arrays.asList(2250, 100, 5600, 400, 2000, 0, 55_000)) {

            System.out.println("----------- TEST " + sum2withdraw + " -----------");

            // Fill ATM with initial amount

            List<Bill> bills = new ArrayList<>();
            Map<Integer, Integer> dataSet = new HashMap<>();
            dataSet.put(50, 10);
            dataSet.put(100, 10);
            dataSet.put(500, 10);
            dataSet.put(2000, 10);
            dataSet.put(5000, 10);
            dataSet.forEach((nominal, count) -> {
                System.out.println(nominal + " " + count);
                for (int i = 0; i < count; i++) {
                    bills.add(new Bill(CurrencyCode.RUB, nominal));
                }
            });
            RuAtm atm = new RuAtm(bills);

            // User work...
            Account acc = new Account(1234567890, CurrencyCode.RUB, 1_000_000);
            atm.enterAtm(acc);

            atm.atmStorageReport();
            int sumOutput = atm.withdraw(sum2withdraw).stream().map(Bill::getValue).reduce((v1, v2) -> v1 + v2).orElse(0);
            atm.atmStorageReport();

            System.out.println("ATM output summ = " + sumOutput + ", account balance = " + acc.getBalance());

            assertEquals(sum2withdraw, sumOutput);
            assertEquals(acc.getBalance(), 1_000_000 - sum2withdraw);
        }
    }

    @Test
    public void test3_withdrawUSD(){

        for(int sum2withdraw : Arrays.asList(1, 13, 1259, 1260, 1261)) {

            System.out.println("----------- TEST " + sum2withdraw + " -----------");

            // Fill ATM with initial amount

            List<Bill> bills = new ArrayList<>();
            Map<Integer, Integer> dataSet = new HashMap<>();
            dataSet.put(1, 10);
            dataSet.put(5, 10);
            dataSet.put(20, 10);
            dataSet.put(100, 10);
            dataSet.forEach((nominal, count) -> {
                System.out.println(nominal + " " + count);
                for (int i = 0; i < count; i++) {
                    bills.add(new Bill(CurrencyCode.USD, nominal));
                }
            });
            UsdAtm atm = new UsdAtm(bills);

            // User work...
            Account acc = new Account(1234567890, CurrencyCode.RUB, 1_000_000);
            atm.enterAtm(acc);

            atm.atmStorageReport();
            int sumOutput = 0;
            try {
                sumOutput = atm.withdraw(sum2withdraw).stream().map(Bill::getValue).reduce((v1, v2) -> v1 + v2).orElse(0);
            } catch(IllegalArgumentException iae){
                iae.printStackTrace();
                assertEquals(acc.getBalance(), 1_000_000);
                return;
            }
            atm.atmStorageReport();

            System.out.println("ATM output summ = " + sumOutput + ", account balance = " + acc.getBalance());

            assertEquals(sum2withdraw, sumOutput);
            assertEquals(acc.getBalance(), 1_000_000 - sum2withdraw);
        }
    }

}
