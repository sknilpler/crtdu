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
public class Ocenka {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Date data;
    private int ball;
    @ManyToOne
    private Kid kid;
    @ManyToOne
    private Krujok krujok;

    public Ocenka(Date data, int ball, Kid kid, Krujok krujok) {
        this.data = data;
        this.ball = ball;
        this.kid = kid;
        this.krujok = krujok;
    }

    @Override
    public String toString() {
        return "Ocenka{" +
                "id=" + id +
                ", data=" + data +
                ", ball=" + ball +
                ", kid=" + kid +
                ", krujok=" + krujok +
                '}';
    }
}
