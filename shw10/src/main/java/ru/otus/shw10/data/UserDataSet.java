package ru.otus.shw10.data;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

import static org.hibernate.annotations.CascadeType.ALL;


@ToString(callSuper = true)
@Data
@Entity
@Table(name = "UserDataSet")
public class UserDataSet extends DataSet {
    @Column(name = "name")
    String name;
    @Column(name = "age")
    int age;

    @OneToOne
    @JoinColumn(name = "id")
    @Cascade(ALL)
    AddressDataSet adress;

    @OneToMany(mappedBy = "userId")
    @Cascade(ALL)
    List<PhoneDataSet> phones;

    public void setAge(Integer age){
        this.age = age;
    }

}
