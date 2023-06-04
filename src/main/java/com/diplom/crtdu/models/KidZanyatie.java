package com.diplom.crtdu.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class KidZanyatie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kid_id")
    private Kid kid;

    @ManyToOne
    @JoinColumn(name = "zanyatie_id")
    private Zanyatie zanyatie;

    private boolean poseshchenie;

    private int otsenka;

    public KidZanyatie(Kid kid, Zanyatie zanyatie, boolean poseshchenie, int otsenka) {
        this.kid = kid;
        this.zanyatie = zanyatie;
        this.poseshchenie = poseshchenie;
        this.otsenka = otsenka;
    }

    public KidZanyatie(Kid kid, Zanyatie zanyatie) {
        this.kid = kid;
        this.zanyatie = zanyatie;
    }

    @Override
    public String toString() {
        return "KidZanyatie{" +
                "id=" + id +
                ", kid=" + kid +
                ", zanyatie=" + zanyatie +
                ", poseshchenie=" + poseshchenie +
                ", otsenka=" + otsenka +
                '}';
    }
}