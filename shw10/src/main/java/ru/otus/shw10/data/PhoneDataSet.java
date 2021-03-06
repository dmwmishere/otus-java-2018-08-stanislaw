package ru.otus.shw10.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "phonedataset")
public class PhoneDataSet extends DataSet {

    @Column(name = "phone")
    private String phone;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private UserDataSet user;

    // compatibility with homework 10;
    private long userId;

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
