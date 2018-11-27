package ru.otus.shw10.data;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class UserDataSet extends DataSet {
    String name;
    int age;
}
