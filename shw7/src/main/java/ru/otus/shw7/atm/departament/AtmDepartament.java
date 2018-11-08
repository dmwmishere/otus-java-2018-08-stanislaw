package ru.otus.shw7.atm.departament;

import ru.otus.shw7.atm.ManagableAtm;
import ru.otus.shw7.currency.CurrencyCode;
import java.util.HashMap;
import java.util.Map;

public class AtmDepartament implements ManagableAtm {

    Map<ManagableAtm, byte[] > atms = new HashMap<>();

    public void registerAtm(ManagableAtm atm) {
        if (atms.containsKey(atm)) {
            System.err.println("This ATM already registered");
        } else {
            atms.put(atm, atm.saveState());
        }
    }

    @Override
    public Map<CurrencyCode, Long> getStorageSum() {
        Map<CurrencyCode, Long> totalSum = new HashMap<>();
        atms.keySet().forEach(atm -> atm.getStorageSum().forEach(((currencyCode, amount) ->
                totalSum.put(currencyCode, (totalSum.containsKey(currencyCode) ? totalSum.get(currencyCode) : 0) + amount)
        )));
        return totalSum;
    }

    public void restoreState(ManagableAtm atm){
        byte [] prevState = atms.get(atm);

        atm.rollback2state(prevState);

    }

    @Override
    public byte [] saveState() {
        return null;
    }

    @Override
    public void rollback2state(byte [] bytes) {

    }
}
