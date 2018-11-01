package ru.otus.shw7.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.shw7.currency.CurrencyCode;

@Data
@AllArgsConstructor
public class Account {
    private long userId;
    private CurrencyCode currencyCode;
    private long balance;
}
