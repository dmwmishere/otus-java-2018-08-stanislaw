package ru.otus.shw10.data;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@ToString(callSuper = true)
@Data
@Entity
@Table(name = "phonedataset")
public class PhoneDataSet extends DataSet{
    @Column(name = "phone")
    String phone;

    @Column(name = "userid")
    long userId;

    public void setUserId(Long userId){
        this.userId = userId;
    }
}
