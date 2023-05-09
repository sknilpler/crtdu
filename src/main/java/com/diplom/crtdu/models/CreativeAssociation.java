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
public class CreativeAssociation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private boolean archive;

    private String pdoSurname;
    private String pdoName;
    private String pdoPatronymic;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "creative_association_id")
    private List<Krujok> krujki;

    /**
     * Творческое объединение.
     *
     * @param name имя творческого объединения
     */
    public CreativeAssociation(String name) {
        this.name = name;
        this.archive = false;
    }

    public CreativeAssociation(String name, String pdoSurname, String pdoName, String pdoPatronymic) {
        this.name = name;
        this.archive = false;
        this.pdoSurname = pdoSurname;
        this.pdoName = pdoName;
        this.pdoPatronymic = pdoPatronymic;
    }

    @Override
    public String toString() {
        return "CreativeAssociation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", archive=" + archive +
                ", pdoSurname='" + pdoSurname + '\'' +
                ", pdoName='" + pdoName + '\'' +
                ", pdoPatronymic='" + pdoPatronymic + '\'' +
                '}';
    }
}
