package ru.otus.shw7.atm.checkHandlers;

import lombok.Getter;
import lombok.Setter;
import ru.otus.shw7.atm.AtmMessage;
import ru.otus.shw7.currency.Bill;

import java.util.List;

abstract public class AbstractDepositCheck {

    @Getter
    @Setter
    private AbstractDepositCheck next;

    public abstract AtmMessage check(List<Bill> bills);

    public AbstractDepositCheck build(AbstractDepositCheck nextCheck){
        setNext(nextCheck);
        return this;
    }

}
