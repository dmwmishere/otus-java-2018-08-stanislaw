package ru.otus.shw10.data;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

import static org.hibernate.annotations.CascadeType.ALL;

@Setter
@Getter
@Entity
@Table(name = "UserDataSet")
public class UserDataSet extends DataSet {

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @OneToOne
    @JoinColumn(name = "id")
    @Cascade(ALL)
    private AddressDataSet adress;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<PhoneDataSet> phones;

    public void setAge(Integer age){
        this.age = age;
    }

}
