package ru.otus.shw10.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addressdataset")
public class AddressDataSet extends DataSet{

    @Column(name = "street")
    private String street;

}
