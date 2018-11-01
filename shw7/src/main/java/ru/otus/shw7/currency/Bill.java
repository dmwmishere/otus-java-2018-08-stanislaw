package ru.otus.shw7.currency;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Bill {

    private final CurrencyCode code;
    private final int value;

    public Bill(final CurrencyCode code, final int value){

        if(!code.getCurrencyNominals().contains(value)){
            throw new IllegalStateException("There is no such bill of " + value + " for " + code);
        } else {
            this.code = code;
            this.value = value;
        }

    }
}
