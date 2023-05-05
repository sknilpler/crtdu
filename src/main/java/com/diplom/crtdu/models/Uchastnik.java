package com.diplom.crtdu.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Uchastnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Kid kid;

    @ManyToOne
    private Krujok krujok;

    @ManyToOne
    private Meropriyatie meropriyatie;

    public Uchastnik(Kid kid, Krujok krujok, Meropriyatie meropriyatie) {
        this.kid = kid;
        this.krujok = krujok;
        this.meropriyatie = meropriyatie;
    }

    @Override
    public String toString() {
        return "Uchastnik{" +
                "id=" + id +
                ", kid=" + kid +
                ", krujok=" + krujok +
                ", meropriyatie=" + meropriyatie +
                '}';
    }
}
