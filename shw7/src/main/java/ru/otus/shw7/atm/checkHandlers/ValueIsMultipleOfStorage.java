package ru.otus.shw7.atm.checkHandlers;

import ru.otus.shw7.atm.AtmMessage;
import ru.otus.shw7.currency.CurrencyCode;

import static java.util.Collections.min;

public class ValueIsMultipleOfStorage extends AbstractWithdrawCheck {

    @Override
    public AtmMessage check(int value, CurrencyCode currencyCode) {
        return value % min(currencyCode.getCurrencyNominals()) == 0 ?
                new AtmMessage(AtmMessage.AtmRC.OK, ""):
                new AtmMessage(AtmMessage.AtmRC.VALUE_ERROR, "VALUE NOT MULTIPLE OF " + min(currencyCode.getCurrencyNominals()));
    }
}
