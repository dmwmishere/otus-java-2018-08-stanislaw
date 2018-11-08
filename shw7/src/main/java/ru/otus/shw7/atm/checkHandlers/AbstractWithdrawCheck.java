package ru.otus.shw7.atm.checkHandlers;

import lombok.Getter;
import lombok.Setter;
import ru.otus.shw7.atm.AtmMessage;
import ru.otus.shw7.currency.CurrencyCode;

abstract public class AbstractWithdrawCheck {

    @Getter
    @Setter
    private AbstractWithdrawCheck next;

    public abstract AtmMessage check(int value, CurrencyCode currencyCode);

    public AbstractWithdrawCheck build(AbstractWithdrawCheck nextCheck){
        setNext(nextCheck);
        return this;
    }
}
