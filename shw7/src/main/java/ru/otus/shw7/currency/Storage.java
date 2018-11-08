package ru.otus.shw7.currency;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * stores a count of each bill value
 */
public class Storage implements Serializable {

    private HashMap<Integer, Long> storage = new HashMap<>();

    /**
     * Get a single bill from the storage
     * @param bill description
     * @return 0 - success, 3 - negative nominal balance, 2 - no such moninal found
     * @exception IllegalArgumentException if storage currency code is different from the bill
     */
    public int withdraw(Bill bill) {
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
    }

    /**
     * Put a single bill into the storage
     * @param bill description
     */
    public void deposit(Bill bill){
            if(storage.containsKey(bill.getValue())){
                long storageValue = storage.get(bill.getValue());
                storage.put(bill.getValue(), ++storageValue);
            } else {
                storage.put(bill.getValue(), 1L);
            }
    }
    public String printStorageReport(){
        return "storage report = " + storage.toString();
    }

    public long getStorageSumm(){
        return storage.entrySet().stream()
                .map(ent -> ent.getValue() * ent.getKey()).reduce((sum1, sum2) -> sum1 + sum2)
                .orElse(0L);
    }

    public Set<Integer> getAvailableNominals(){
        return storage.keySet();
    }

    public long getBillsCountByValue(int value){
        return storage.get(value);
    }

}
