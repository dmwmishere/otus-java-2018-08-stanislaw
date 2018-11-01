package ru.otus.shw7.atm;

import ru.otus.shw7.currency.Bill;
import ru.otus.shw7.currency.CurrencyCode;
import ru.otus.shw7.user.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Collections.*;

public class RuAtm extends AbstractAtm {

    private Account account;

    public RuAtm() {
        super(CurrencyCode.RUB);
    }

    public RuAtm(List<Bill> initialMoney){
        super(CurrencyCode.RUB);
        // initially deposit into ATM with system account
        account = new Account(0, storage.getStorageCurrency(), Long.MAX_VALUE);
        deposit(initialMoney);
        account = null;
    }

    public void enterAtm(Account account) {
        this.account = account;
    }

    public void quitAtm() {
        if (account == null) throw new IllegalStateException("No session running");
        this.account = null;
    }

    @Override
    public List<Bill> withdraw(final int value) {

        if (value > storage.getStorageSumm())
            throw new IllegalArgumentException("Required value is higher than storage summ!");

        Set<Integer> storageNominals = new TreeSet<>(storage.getStorageCurrency().getCurrencyNominals());
        if (value % min(storageNominals) != 0) {
            throw new IllegalArgumentException("Invalid value to store. " + value + " must divide by " + min(storageNominals));
        }

        List<Bill> bills = new ArrayList<>();

        int tmpValue = value;

        while (tmpValue > 0) {
            int maxNominal = max(storageNominals);
//            System.out.println(String.format("get nominal %d from left value of %d, storage summ = %d",
//                    maxNominal, tmpValue, storage.getStorageSumm()));
            Bill bill = new Bill(storage.getStorageCurrency(), maxNominal);
            if(maxNominal > tmpValue){
                storageNominals.remove(maxNominal);
                continue;
            }
            if(storage.withdraw(bill) == 0) {
                bills.add(bill);
                tmpValue -= maxNominal;
            }
            else
                storageNominals.remove(maxNominal);
        }

        account.setBalance(account.getBalance() - value);

        return bills;
    }

    @Override
    public void deposit(List<Bill> bills) {

        if (bills.stream().anyMatch(bill -> bill.getCode() != storage.getStorageCurrency())) {
            throw new IllegalArgumentException("Bill with currency different from storage currency presented!");
        }

        bills.forEach(storage::deposit);

        account.setBalance(account.getBalance() +
                bills.stream().map(Bill::getValue).reduce((b1, b2) -> b1 + b2).orElse(0));
    }

    @Override
    public long balance() {
        return account.getBalance();
    }

    public void printCheck() {
        System.out.println(account.getUserId() + " balance = " + balance() + " " + account.getCurrencyCode());
    }

    public void atmStorageReport() {
        storage.printStorageReport();
    }

}
