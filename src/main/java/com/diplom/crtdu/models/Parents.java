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

    @ManyToOne
    private Kid kid;

    @ManyToMany
    @JoinTable(name = "kid_parent",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "kid_id"))
    private List<Kid> kids;

    public Parents(String surname, String name, String patronymic, String adres, String phone) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.adres = adres;
        this.phone = phone;
    }

    public Parents(String surname, String name, String patronymic, String adres, String phone, Kid kid) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.adres = adres;
        this.phone = phone;
        this.kid = kid;
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
                ", kid=" + kid +
                '}';
    }
}
