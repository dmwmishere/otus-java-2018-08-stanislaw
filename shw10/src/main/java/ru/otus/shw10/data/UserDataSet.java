package ru.otus.shw10.data;

import lombok.Data;
import lombok.ToString;

@ToString(callSuper = true)
@Data
public class UserDataSet extends DataSet {
    String name;
    int age;

    public void setAge(Integer age){
        this.age = age;
    }

}
