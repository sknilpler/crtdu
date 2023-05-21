package com.diplom.crtdu.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class TypeKrujok {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_krujok_id")
    private List<Krujok> krujki;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_krujok_id")
    private List<Norma> norma;

    public TypeKrujok(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TypeKrujok{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
