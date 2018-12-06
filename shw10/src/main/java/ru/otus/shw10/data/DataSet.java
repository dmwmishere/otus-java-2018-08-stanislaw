package ru.otus.shw10.data;

import lombok.Data;
import javax.persistence.*;

@Data
@MappedSuperclass
public abstract class DataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    public void setId(Long id){
        this.id = id;
    }
}
