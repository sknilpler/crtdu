package com.diplom.crtdu.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Kid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String surname;
    private String name;
    private String patronymic;

    private Date birthday;


    /**
     * true - мужской
     * false - женский
     */
    private boolean sex;

    private String grazhdanstvo;
    private String adres;
    private String phone;
    private String school;
    private String klas;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany
    @JoinTable(name = "kid_parent",
            joinColumns = @JoinColumn(name = "kid_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id"))
    private List<Parents> parents;

    public Kid(String surname, String name, String patronymic, Date birthday, boolean sex, String grazhdanstvo, String adres, String phone, String school, String klas) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.sex = sex;
        this.grazhdanstvo = grazhdanstvo;
        this.adres = adres;
        this.phone = phone;
        this.school = school;
        this.klas = klas;
    }

    @Override
    public String toString() {
        return "Kid{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthday=" + birthday +
                ", sex=" + (sex ? " мужской" : " женский") +
                ", grazhdanstvo='" + grazhdanstvo + '\'' +
                ", adres='" + adres + '\'' +
                ", phone='" + phone + '\'' +
                ", school='" + school + '\'' +
                ", klas='" + klas + '\'' +
                ", user=" + user.getUsername() +
                '}';
    }
}
