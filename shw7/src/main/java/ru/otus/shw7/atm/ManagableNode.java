package ru.otus.shw7.atm;

import ru.otus.shw7.currency.CurrencyCode;

import java.util.Map;

public interface ManagableNode {
    Map<CurrencyCode, Long> getStorageSum();
    void saveState();
    void rollback2state();
}
