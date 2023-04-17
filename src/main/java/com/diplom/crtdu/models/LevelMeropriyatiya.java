package com.diplom.crtdu.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class LevelMeropriyatiya {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    public LevelMeropriyatiya(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LevelMeropriyatiya{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
