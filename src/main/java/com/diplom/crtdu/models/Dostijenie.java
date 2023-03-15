package com.diplom.crtdu.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Dostijenie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String winPlace;

    @ManyToOne
    private Meropriyatie meropriyatie;

    @ManyToOne
    private Kid kid;

    @ManyToOne
    private Teacher teacher;

    public Dostijenie(String name, String winPlace, Meropriyatie meropriyatie, Kid kid, Teacher teacher) {
        this.name = name;
        this.winPlace = winPlace;
        this.meropriyatie = meropriyatie;
        this.kid = kid;
        this.teacher = teacher;
    }

    public Dostijenie(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dostijenie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", winPlace='" + winPlace + '\'' +
                ", meropriyatiye=" + meropriyatie.getName() +
                ", kid=" + kid.getShortFIO() +
                ", teacher=" + teacher.getShortFIO() +
                '}';
    }
}
