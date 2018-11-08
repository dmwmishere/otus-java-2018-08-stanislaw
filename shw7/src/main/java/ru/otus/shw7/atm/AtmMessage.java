package ru.otus.shw7.atm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public class AtmMessage {

    public enum AtmRC {OK, MALFUNCTION, VALUE_ERROR, PIN_ERROR, CURRENCY_ERROR};

    private final AtmRC resultCode;
    private final String printableText;

}
