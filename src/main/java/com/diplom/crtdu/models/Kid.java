package com.diplom.crtdu.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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


}
