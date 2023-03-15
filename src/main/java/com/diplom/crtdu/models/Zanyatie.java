package com.diplom.crtdu.models;

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
public class Zanyatie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Date dataZan;
    private String mesto;

    @ManyToOne
    private Krujok krujok;

    @ManyToMany
    @JoinTable(name = "kid_zanyatie",
            joinColumns = @JoinColumn(name = "zanyatie_id"),
            inverseJoinColumns = @JoinColumn(name = "kid_id"))
    private List<Kid> kids;

    public Zanyatie(Date dataZan, String mesto, Krujok krujok) {
        this.dataZan = dataZan;
        this.mesto = mesto;
        this.krujok = krujok;
    }

    @Override
    public String toString() {
        return "Zanyatie{" +
                "id=" + id +
                ", dataZan=" + dataZan +
                ", mesto='" + mesto + '\'' +
                ", krujok=" + krujok +
                '}';
    }
}
