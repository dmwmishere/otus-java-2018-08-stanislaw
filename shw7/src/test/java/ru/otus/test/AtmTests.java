package ru.otus.test;

import org.junit.Test;
import ru.otus.shw7.atm.*;
import ru.otus.shw7.atm.departament.AtmDepartament;
import ru.otus.shw7.currency.Bill;
import ru.otus.shw7.currency.CurrencyCode;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class AtmTests {

    @Test
    public void test0_depositAll(){
        RuAtm atm = new RuAtm();

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
    }

    @Test (expected = IllegalArgumentException.class)
    public void test1_depositInvalid(){
        RuAtm atm = new RuAtm();

        List<Bill> bills = new ArrayList<>();

        bills.add(new Bill(CurrencyCode.RUB, 50));
        bills.add(new Bill(CurrencyCode.USD, 5));

        atm.deposit(bills);

    }

    @Test
    public void test2_depositZero(){
        RuAtm atm = new RuAtm();

        List<Bill> bills = new ArrayList<>();

        atm.deposit(bills);

        atm.atmStorageReport();

    }


    @Test
    public void test3_withdrawRUB() throws InterruptedException{

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
                for (int i = 0; i < count; i++) {
                    bills.add(new Bill(CurrencyCode.RUB, nominal));
                }
            });
            AbstractAtm atm = new RuAtm(bills);

            // User work...
            atm.atmStorageReport();
            int sumOutput = atm.withdraw(sum2withdraw).stream().map(Bill::getValue).reduce((v1, v2) -> v1 + v2).orElse(0);
            Thread.sleep(20);
            atm.atmStorageReport();

            System.out.println("ATM output summ = " + sumOutput);

            assertEquals(sum2withdraw, sumOutput);
        }
    }

    @Test
    public void test3_withdrawUSD() throws InterruptedException{

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
            AbstractAtm atm = new UsdAtm(bills);

            // User work...
            atm.atmStorageReport();
            int sumOutput = 0;
            try {
                Thread.sleep(10);
                sumOutput = atm.withdraw(sum2withdraw).stream().map(Bill::getValue).reduce((v1, v2) -> v1 + v2).orElse(0);
            } catch(IllegalArgumentException iae){
                iae.printStackTrace();
                return;
            }
            atm.atmStorageReport();

            System.out.println("ATM output summ = " + sumOutput);

            assertEquals(sum2withdraw, sumOutput);
        }
    }

    @Test
    public void test0_ManagableAtm_methods(){
        List<Bill> usdBills = new ArrayList<>();
        Map<Integer, Integer> dataSet = new HashMap<>();
        dataSet.put(1, 10);
        dataSet.put(5, 10);
        dataSet.put(20, 10);
        dataSet.put(100, 10);
        dataSet.forEach((nominal, count) -> {
            for (int i = 0; i < count; i++) {
                usdBills.add(new Bill(CurrencyCode.USD, nominal));
            }
        });

        List<Bill> rubBills = new ArrayList<>();
        dataSet.clear();
        dataSet.put(50, 10);
        dataSet.put(500, 10);
        dataSet.put(2000, 10);
        dataSet.put(5000, 10);
        dataSet.forEach((nominal, count) -> {
            for (int i = 0; i < count; i++) {
                rubBills.add(new Bill(CurrencyCode.RUB, nominal));
            }
        });

        AtmDepartament atmDep = new AtmDepartament();

        atmDep.registerAtm(new UsdAtm(usdBills));
        atmDep.registerAtm(new UsdAtm(usdBills));

        atmDep.registerAtm(new RuAtm(rubBills));
        atmDep.registerAtm(new RuAtm(rubBills));

        System.out.println("First departament = " + atmDep.getStorageSum());

        AtmDepartament atmDep2 = new AtmDepartament();

        atmDep2.registerAtm(new UsdAtm(usdBills));
        atmDep2.registerAtm(new UsdAtm(usdBills));

        atmDep2.registerAtm(new RuAtm(rubBills));
        atmDep2.registerAtm(new RuAtm(rubBills));

        System.out.println("Second departament = " + atmDep2.getStorageSum());

        AtmDepartament chiefDep = new AtmDepartament();

        chiefDep.registerAtm(atmDep);
        chiefDep.registerAtm(atmDep2);

        System.out.println("Chief departament = " + chiefDep.getStorageSum());

    }

    @Test
    public void test0_atmState(){

        AbstractAtm atm = new RuAtm();

        atm.deposit(Collections.singletonList(new Bill(CurrencyCode.RUB, 100)));

        atm.saveState();

        System.out.println("1 = " + atm.getStorageSum());

        AtmDepartament atmDep = new AtmDepartament();
        atmDep.registerAtm(atm);

        atm.deposit(Collections.singletonList(new Bill(CurrencyCode.RUB, 100)));
        atm.deposit(Collections.singletonList(new Bill(CurrencyCode.RUB, 500)));

        System.out.println("2 = " + atm.getStorageSum());

        atmDep.restoreState(atm);

        System.out.println("3 = " + atm.getStorageSum());

        atm.deposit(Collections.singletonList(new Bill(CurrencyCode.RUB, 1000)));
        atm.deposit(Collections.singletonList(new Bill(CurrencyCode.RUB, 5000)));

        System.out.println("3 = " + atm.getStorageSum());
    }


}
