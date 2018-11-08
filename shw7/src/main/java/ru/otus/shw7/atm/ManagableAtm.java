package ru.otus.shw7.atm;

import ru.otus.shw7.currency.CurrencyCode;

import java.io.Serializable;
import java.util.Map;

public interface ManagableAtm extends Serializable {
    Map<CurrencyCode, Long> getStorageSum();
    byte [] saveState();
    void rollback2state(byte [] bytes);
}
