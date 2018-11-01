package ru.otus.shw7.currency;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * stores a count of each bill value
 */
public class Storage {

    @Getter
    private final CurrencyCode storageCurrency;

    private HashMap<Integer, Long> storage = new HashMap<>();

    public Storage(CurrencyCode storageCurrency){
        this.storageCurrency = storageCurrency;
    }

    /**
     * Get a single bill from the storage
     * @param bill description
     * @return 0 - success, 3 - negative nominal balance, 2 - no such moninal found
     * @exception IllegalArgumentException if storage currency code is different from the bill
     */
    public int withdraw(Bill bill) {
        if(isBillValid(bill)){
            if(storage.containsKey(bill.getValue())){
                long storageValue = storage.get(bill.getValue());
                if(storageValue - 1 < 0){
                    return 3;
                } else {
                    storage.put(bill.getValue(), --storageValue);
                    return 0;
                }
            } else {
                return 2;
            }
        } else {
            throw new IllegalArgumentException("Bill of " + bill.getCode() + " not belongs to " + storageCurrency + " storage!");
        }
    }

    /**
     * Put a single bill into the storage
     * @param bill description
     */
    public void deposit(Bill bill){
        if(isBillValid(bill)){
            if(storage.containsKey(bill.getValue())){
                long storageValue = storage.get(bill.getValue());
                storage.put(bill.getValue(), ++storageValue);
            } else {
                storage.put(bill.getValue(), 1L);
            }
        } else {
            throw new IllegalArgumentException("Bill of " + bill.getCode() + " not belongs to " + storageCurrency + " storage!");
        }
    }

    private boolean isBillValid(Bill bill){
        return bill.getCode() == storageCurrency;
    }

    public Map<Integer, Long> printStorageReport(){
        System.out.println("Storage report = " + storage.toString());
        return storage;
    }

    public long getStorageSumm(){
        return storage.entrySet().stream()
                .map(ent -> ent.getValue() * ent.getKey()).reduce((sum1, sum2) -> sum1 + sum2)
                .orElse(0L);
    }

    public long getBillsCountByValue(int value){
        return storage.get(value);
    }

}
