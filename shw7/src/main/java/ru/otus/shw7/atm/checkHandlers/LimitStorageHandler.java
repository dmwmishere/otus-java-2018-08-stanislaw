package ru.otus.shw7.atm.checkHandlers;

import lombok.RequiredArgsConstructor;
import ru.otus.shw7.atm.AbstractAtm;
import ru.otus.shw7.atm.AtmMessage;
import ru.otus.shw7.currency.CurrencyCode;

// WITHDRAW DECORATOR
@RequiredArgsConstructor
public class LimitStorageHandler extends AbstractWithdrawCheck {

    private final AbstractAtm atm;

    private final static AtmMessage failMsg =
            new AtmMessage(AtmMessage.AtmRC.VALUE_ERROR, "SUM EXCEED AVAILABLE STORAGE VALUE");

    @Override
    public AtmMessage check(int value, CurrencyCode currencyCode) {
        AtmMessage msg = new AtmMessage(AtmMessage.AtmRC.OK, "");
        if(getNext() != null){ // if pipeline contains any other handlers check them first;
            msg = getNext().check(value, currencyCode);
        }
        if(msg.getResultCode() != AtmMessage.AtmRC.OK) // if other handler check failed return the result
            return msg;
        else // otherwise perform this check;
            return atm.storageValueByCurrency(currencyCode) > value ?
                new AtmMessage(AtmMessage.AtmRC.OK, "") :
                failMsg;
    }
}
