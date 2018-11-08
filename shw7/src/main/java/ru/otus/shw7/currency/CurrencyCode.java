package ru.otus.shw7.currency;

import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public enum CurrencyCode implements Serializable {
    USD(840, new TreeSet<>(Arrays.asList(1, 2, 5, 10, 20, 50, 100))),
    EUR(978, new TreeSet<>(Arrays.asList(5, 10, 20, 50, 100, 200, 500))),
    RUB(643, new TreeSet<>(Arrays.asList(50, 100, 200, 500, 1000, 2000, 5000)));

    private final int isoCode;

    @Getter
    private final Set<Integer> currencyNominals;

    CurrencyCode(int isoCode, Set<Integer> currencyNominals){
        this.isoCode = isoCode;
        this.currencyNominals = currencyNominals;
    }

    int getCurrencyCode(){
        return isoCode;
    }

}
