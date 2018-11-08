package ru.otus.shw7.atm;

import ru.otus.shw7.atm.checkHandlers.*;
import ru.otus.shw7.currency.Bill;
import ru.otus.shw7.currency.CurrencyCode;
import java.util.List;

public class UsdAtm extends AbstractAtm {

    private final static CurrencyCode atmCurrency = CurrencyCode.USD;

    private final AbstractDepositCheck depositCheckPipeline = new MaxDepositCheckHandler().build( new CurrencyCheckHandler(this));
    private final AbstractWithdrawCheck withdrawCheckPipeline = new LimitStorageHandler(this).build(new ValueIsMultipleOfStorage());

    public UsdAtm() {
        super(atmCurrency);
        setDepositChecksPipeline(depositCheckPipeline);
    }

    public UsdAtm(List<Bill> initialMoney){
        super(atmCurrency);
        deposit(initialMoney);
    }

    public List<Bill> withdraw(final int value) {
        setWithdrawChecksPipeline(withdrawCheckPipeline);
        return withdraw(value, atmCurrency);
    }

    @Override
    public void deposit(List<Bill> bills) {
        AtmMessage msg = depositCheckPipeline.check(bills);
        if(msg.getResultCode() != AtmMessage.AtmRC.OK){
            throw new IllegalArgumentException(msg.toString());
        }
        bills.forEach(storage.get(atmCurrency)::deposit);
    }
}
