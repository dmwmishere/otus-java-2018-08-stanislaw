package ru.otus.shw10.data;

import lombok.Data;
import lombok.ToString;

@ToString(callSuper = true)
@Data
public class PhoneDataSet extends DataSet{
    String phone;
    long userId;

    public void setUserId(Long userId){
        this.userId = userId;
    }
}
