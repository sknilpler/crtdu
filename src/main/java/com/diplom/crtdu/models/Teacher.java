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
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String surname;
    private String name;
    private String patronymic;
    private String doljnost;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private List<Dostijenie> dostList;

    @ManyToMany
    @JoinTable(name = "teacher_krujok",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "krujok_id"))
    private List<Krujok> krujki;

    public Teacher(String surname, String name, String patronymic, String doljnost) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.doljnost = doljnost;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", doljnost='" + doljnost + '\'' +
                '}';
    }

    public String getFullFIO() {
        return surname + " " + name + " " + patronymic;
    }

    public String getShortFIO() {
        return surname + " " + name.charAt(0) + " " + patronymic.charAt(0);
    }
}
