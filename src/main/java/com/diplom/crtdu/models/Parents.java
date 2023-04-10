package com.diplom.crtdu.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Parents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String surname;
    private String name;
    private String patronymic;

    private String adres;
    private String phone;
    private String email;

    @ManyToMany
    @JoinTable(name = "kid_parent",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "kid_id"))
    private List<Kid> kids;

    public Parents(String surname, String name, String patronymic, String adres, String phone, String email) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.adres = adres;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Parents{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", adres='" + adres + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getFullFIO() {
        return surname + " " + name + " " + patronymic;
    }

    public String getShortFIO() {
        return surname + " " + name.charAt(0) + " " + patronymic.charAt(0);
    }
}
