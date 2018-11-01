package ru.otus.shw7.atm;

import ru.otus.shw7.currency.Bill;
import ru.otus.shw7.currency.CurrencyCode;
import ru.otus.shw7.currency.Storage;

import java.util.List;

abstract public class AbstractAtm {

    protected final Storage storage;

    AbstractAtm(CurrencyCode currencyCode){
        storage = new Storage(currencyCode);
    }

    abstract public List<Bill> withdraw(int value);

    abstract public void deposit(List<Bill> bills);

    abstract public long balance();

}
