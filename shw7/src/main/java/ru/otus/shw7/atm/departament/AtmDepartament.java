package ru.otus.shw7.atm.departament;

import org.apache.commons.lang.NotImplementedException;
import ru.otus.shw7.atm.ManagableNode;
import ru.otus.shw7.currency.CurrencyCode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AtmDepartament implements ManagableNode {

    Set<ManagableNode > atms = new HashSet<>();

    public void registerAtm(ManagableNode atm) {
        if (atms.contains(atm)) {
            System.err.println("This ATM already registered");
        } else {
            atms.add(atm);
        }
    }

    @Override
    public Map<CurrencyCode, Long> getStorageSum() {
        Map<CurrencyCode, Long> totalSum = new HashMap<>();
        atms.forEach(atm -> atm.getStorageSum().forEach(((currencyCode, amount) ->
                totalSum.put(currencyCode, (totalSum.containsKey(currencyCode) ? totalSum.get(currencyCode) : 0) + amount)
        )));
        return totalSum;
    }

    public void restoreState(ManagableNode atm){
        atm.rollback2state();
    }

    @Override
    public void saveState() {
        throw new NotImplementedException();
    }

    @Override
    public void rollback2state() {
        throw new NotImplementedException();
    }
}
