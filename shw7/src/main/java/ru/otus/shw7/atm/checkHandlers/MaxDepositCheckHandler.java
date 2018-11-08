package ru.otus.shw7.atm.checkHandlers;

import ru.otus.shw7.atm.AtmMessage;
import ru.otus.shw7.currency.Bill;

import java.util.List;

public class MaxDepositCheckHandler extends AbstractDepositCheck {

    @Override
    public AtmMessage check(List<Bill> bills) {
        AtmMessage msg = new AtmMessage(AtmMessage.AtmRC.OK, "");
        if(getNext() != null){ // if pipeline contains any other handlers check them first;
            msg = getNext().check(bills);
        }
        if(msg.getResultCode() != AtmMessage.AtmRC.OK) // if other handler check failed return the result
            return msg;
        return bills.size() < 1000 ? new AtmMessage(AtmMessage.AtmRC.OK, "")
                : new AtmMessage(AtmMessage.AtmRC.VALUE_ERROR, "TOO MUCH BANKNOTES INSERTED");
    }
}
