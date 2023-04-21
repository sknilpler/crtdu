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
public class TypeMeropriyatiya {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id")
    private List<Meropriyatie> meropriyaties;

    public TypeMeropriyatiya(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TypeMeropriyatiya{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
