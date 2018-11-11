package ru.otus.shw7.atm;

import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;
import ru.otus.shw7.atm.checkHandlers.AbstractDepositCheck;
import ru.otus.shw7.atm.checkHandlers.AbstractWithdrawCheck;
import ru.otus.shw7.currency.Bill;
import ru.otus.shw7.currency.CurrencyCode;
import ru.otus.shw7.currency.Storage;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.util.Collections.max;

abstract public class AbstractAtm implements AtmBasicOperation, ManagableNode {

    private static final AtomicLong atmSeq = new AtomicLong();

    protected HashMap<CurrencyCode, Storage> storage;

    private final long atmId = atmSeq.getAndIncrement();

    private byte [] prevState = null;

    @Setter
    private AbstractDepositCheck depositChecksPipeline;

    @Setter
    private AbstractWithdrawCheck withdrawChecksPipeline;

    AbstractAtm(CurrencyCode ... currencyCode){
        storage = new HashMap<>();
        for(CurrencyCode currency : currencyCode){
            storage.put(currency, new Storage());
        }
    }

    public long storageValueByCurrency(CurrencyCode currencyCode){
        return storage.get(currencyCode).getStorageSumm();
    }

    public boolean isCurrencySupported(CurrencyCode currencyCode){
        return storage.get(currencyCode) != null;
    }

    public List<Bill> withdraw(final int value, CurrencyCode currencyCode){
        Set<Integer> storageNominals = storage.get(currencyCode).getAvailableNominals();
        List<Bill> bills = new ArrayList<>();
        if(withdrawChecksPipeline != null){
            AtmMessage msg = withdrawChecksPipeline.check(value, currencyCode);
            if(msg.getResultCode() != AtmMessage.AtmRC.OK) throw new IllegalArgumentException(msg.toString());
        }
        int tmpValue = value;
        while (tmpValue > 0) {
            int maxNominal = max(storageNominals);
            Bill bill = new Bill(currencyCode, maxNominal);
            if(maxNominal > tmpValue){
                storageNominals.remove(maxNominal);
                continue;
            }
            if(storage.get(currencyCode).withdraw(bill) == 0) {
                bills.add(bill);
                tmpValue -= maxNominal;
            }
            else
                storageNominals.remove(maxNominal);
        }
        return bills;
    }

    abstract public List<Bill> withdraw(int value);

    abstract public void deposit(List<Bill> bills);

    public void atmStorageReport() {
        storage.forEach((currencyCode, storage1) -> System.out.println(currencyCode + ", " +storage1.printStorageReport()));
    }

    @Override
    public Map<CurrencyCode, Long> getStorageSum() {
        return storage.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                entry -> entry.getValue().getStorageSumm()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractAtm that = (AbstractAtm) o;
        return atmId == that.atmId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(atmId);
    }

    @Override
    public void saveState(){
        prevState = SerializationUtils.serialize(storage);
    }

    @Override
    public void rollback2state() {
        storage = (HashMap<CurrencyCode, Storage>) SerializationUtils.deserialize(prevState);
    }


}
