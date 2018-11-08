package ru.otus.shw7.atm;

import ru.otus.shw7.currency.Bill;
import ru.otus.shw7.currency.CurrencyCode;

import java.util.List;

public interface AtmBasicOperation {

    List<Bill> withdraw(final int value, CurrencyCode currencyCode);

    void deposit(List<Bill> bills);

}
