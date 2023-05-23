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
    private boolean archive;

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

//    @ManyToMany
//    @JoinTable(name = "krujok_meropriyatie",
//            joinColumns = @JoinColumn(name = "krujok_id"),
//            inverseJoinColumns = @JoinColumn(name = "meropriyatie_id"))
//    private List<Meropriyatie> meropriyatiya;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "krujok_id")
    private List<Ocenka> marks;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "krujok_id")
    private List<Raspisanie> raspisanie;

    @ManyToOne
    private TypeKrujok typeKrujok;

    @ManyToOne
    private CreativeAssociation creativeAssociation;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "krujok_id")
    private List<Zanyatie> zanyatiya;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "krujok_id")
    private List<Uchastnik> meropriyatiya;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "krujok_id")
    private List<Dostijenie> dostList;

    public Krujok(String name, String vozrast, TypeKrujok type) {
        this.name = name;
        this.vozrast = vozrast;
        this.typeKrujok = type;
        this.archive = false;
    }

    public Krujok(String name, String vozrast, TypeKrujok typeKrujok, CreativeAssociation creativeAssociation) {
        this.name = name;
        this.vozrast = vozrast;
        this.typeKrujok = typeKrujok;
        this.creativeAssociation = creativeAssociation;
        this.archive = false;
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
