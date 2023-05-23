package com.diplom.crtdu.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Raspisanie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;

    @ManyToOne
    private Teacher teacher;
    @ManyToOne
    private Krujok krujok;

    public Raspisanie(String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday, Teacher teacher, Krujok krujok) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.teacher = teacher;
        this.krujok = krujok;
    }

    @Override
    public String toString() {
        return "Raspisanie{" +
                "id=" + id +
                ", monday='" + monday + '\'' +
                ", tuesday='" + tuesday + '\'' +
                ", wednesday='" + wednesday + '\'' +
                ", thursday='" + thursday + '\'' +
                ", friday='" + friday + '\'' +
                ", saturday='" + saturday + '\'' +
                ", sunday='" + sunday + '\'' +
                ", teacher=" + teacher +
                ", krujok=" + krujok +
                '}';
    }
}
