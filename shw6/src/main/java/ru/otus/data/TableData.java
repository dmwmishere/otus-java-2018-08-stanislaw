package ru.otus.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableData {

    int id;

    String value;

    // make the object heavy
    private byte [] bytes = new byte[1024*1024];

    @Override
    public String toString(){
        return String.valueOf(id).concat(" = " + value);
    }

}
