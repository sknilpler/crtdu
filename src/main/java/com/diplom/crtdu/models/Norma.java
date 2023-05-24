package com.diplom.crtdu.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Norma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String age;
    private int hoursPerWeek;
    private int hoursPerDay;

    @ManyToOne
    private TypeKrujok typeKrujok;

    public Norma(String age, int hoursPerWeek, int hoursPerDay, TypeKrujok typeKrujok) {
        this.age = age;
        this.hoursPerWeek = hoursPerWeek;
        this.hoursPerDay = hoursPerDay;
        this.typeKrujok = typeKrujok;
    }

    @Override
    public String toString() {
        return "Norma{" +
                "id=" + id +
                ", age='" + age + '\'' +
                ", hoursPerWeek=" + hoursPerWeek +
                ", hoursPerDay=" + hoursPerDay +
                ", typeKrujok=" + typeKrujok +
                '}';
    }

//    public String ageGroup() {
//        int age = Integer.parseInt(this.age);
//        if (age == 1) {
//            return "3-5 лет";
//        } else if (age == 2) {
//            return "6-8 лет";
//        } else if (age == 3) {
//            return "9-11 лет";
//        } else if (age == 4) {
//            return "12-15 лет";
//        } else if (age == 5) {
//            return "16-17 лет";
//        } else {
//            return "Неизвестная возрастная группа";
//        }
//    }
}
