package ru.otus.shw10.data;

import lombok.Data;
import lombok.ToString;

@ToString(callSuper = true)
@Data
public class ExtendedUserDataSet extends UserDataSet {

    int gender;

    String education;

    public void setGender(Integer gender){
        this.gender = gender;
    }

}
