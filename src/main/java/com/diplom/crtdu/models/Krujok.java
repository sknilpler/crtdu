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
public class Krujok {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String vozrast;

    @ManyToMany
    @JoinTable(name = "teacher_krujok",
            joinColumns = @JoinColumn(name = "krujok_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    private List<Teacher> teachers;

    @ManyToMany
    @JoinTable(name = "kid_krujok",
            joinColumns = @JoinColumn(name = "krujok_id"),
            inverseJoinColumns = @JoinColumn(name = "kid_id"))
    private List<Kid> kids;

    @ManyToOne
    private TypeKrujok typeKrujok;

    @ManyToOne
    private CreativeAssociation creativeAssociation;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "krujok_id")
    private List<Zanyatie> zanyatiya;

    public Krujok(String name, String vozrast, TypeKrujok type) {
        this.name = name;
        this.vozrast = vozrast;
        this.typeKrujok = type;
    }

    public Krujok(String name, String vozrast, TypeKrujok typeKrujok, CreativeAssociation creativeAssociation) {
        this.name = name;
        this.vozrast = vozrast;
        this.typeKrujok = typeKrujok;
        this.creativeAssociation = creativeAssociation;
    }

    @Override
    public String toString() {
        return "Krujok{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", vozrast='" + vozrast + '\'' +
                ", type=" + typeKrujok.getName() +
                '}';
    }
}
