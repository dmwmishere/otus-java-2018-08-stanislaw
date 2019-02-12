package ru.otus.shw16.app.messages.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ru.otus.shw16.app.Addressee;

import java.util.UUID;

public abstract class Envelope {

    @Getter
    @Setter
    @JsonIgnore
    private UUID correlationId;

    public abstract void exec(Addressee addressee);

}
