package ru.otus.shw10.data;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class PhoneDataSet extends DataSet{
    String phone;
    long userId;
}
