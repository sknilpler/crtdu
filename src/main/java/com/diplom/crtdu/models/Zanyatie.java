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
    private int dlitelnost;
    private String mesto;

    @ManyToOne
    private Krujok krujok;

//    @ManyToMany
//    @JoinTable(name = "kid_zanyatie",
//            joinColumns = @JoinColumn(name = "zanyatie_id"),
//            inverseJoinColumns = @JoinColumn(name = "kid_id"))
//    private List<Kid> kids;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "zanyatie_id")
    private List<KidZanyatie> kidZanyatia;

    /**
     * Объект занятие
     *
     * @param dataZan дата и время проведения
     * @param dlitelnost длительность в минутах
     * @param mesto место проведения
     * @param krujok кружок по которому будет занятие
     */
    public Zanyatie(Date dataZan, int dlitelnost, String mesto, Krujok krujok) {
        this.dataZan = dataZan;
        this.dlitelnost = dlitelnost;
        this.mesto = mesto;
        this.krujok = krujok;
    }

    @Override
    public String toString() {
        return "Zanyatie{" +
                "id=" + id +
                ", dataZan=" + dataZan +
                ", dlitelnost=" + dlitelnost +
                ", mesto='" + mesto + '\'' +
                ", krujok=" + krujok +
                '}';
    }
}
