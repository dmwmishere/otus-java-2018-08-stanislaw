package ru.otus.shw7.atm.checkHandlers;

import lombok.RequiredArgsConstructor;
import ru.otus.shw7.atm.AbstractAtm;
import ru.otus.shw7.atm.AtmMessage;
import ru.otus.shw7.currency.Bill;
import ru.otus.shw7.currency.CurrencyCode;

import java.util.List;

// DEPOSIT DECORATOR
@RequiredArgsConstructor
public class CurrencyCheckHandler extends AbstractDepositCheck {

    private final AtmMessage failMsg =
            new AtmMessage(AtmMessage.AtmRC.CURRENCY_ERROR, "INVALID CURRENCY INPUT");

    private final AbstractAtm atm;

    @Override
    public AtmMessage check(List<Bill> bills) {        AtmMessage msg = new AtmMessage(AtmMessage.AtmRC.OK, "");
        if(getNext() != null){ // if pipeline contains any other handlers check them first;
            msg = getNext().check(bills);
        }
        if(msg.getResultCode() != AtmMessage.AtmRC.OK) // if other handler check failed return the result
            return msg;
        else // otherwise perform this check;
        return bills.stream().filter(bill -> atm.isCurrencySupported(bill.getCode())).count() == bills.size() ?
                new AtmMessage(AtmMessage.AtmRC.OK, "") : failMsg;
    }
}
