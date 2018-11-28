package ru.otus.shw10.data;

import lombok.Data;

@Data
public abstract class DataSet {
    long id;

    public void setId(Long id){
        this.id = id;
    }
}
