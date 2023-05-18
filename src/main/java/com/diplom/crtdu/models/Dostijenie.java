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
public class Dostijenie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String winPlace;
    private String napravlenie;
    private Date data;

    @ManyToOne
    private Meropriyatie meropriyatie;

    @ManyToOne
    private Kid kid;

    @ManyToOne
    private Teacher teacher;

    @ManyToOne
    private Krujok krujok;


    public Dostijenie(String name, String winPlace, String napravlenie, Date data, Meropriyatie meropriyatie, Kid kid, Teacher teacher, Krujok krujok) {
        this.name = name;
        this.winPlace = winPlace;
        this.napravlenie = napravlenie;
        this.data = data;
        this.meropriyatie = meropriyatie;
        this.kid = kid;
        this.teacher = teacher;
        this.krujok = krujok;
    }

    @Override
    public String toString() {
        return "Dostijenie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", winPlace='" + winPlace + '\'' +
                ", napravlenie='" + napravlenie + '\'' +
                ", data=" + data +
                ", meropriyatie=" + meropriyatie +
                ", kid=" + kid +
                ", teacher=" + teacher +
                '}';
    }
}
