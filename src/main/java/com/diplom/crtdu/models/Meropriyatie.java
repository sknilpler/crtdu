package com.diplom.crtdu.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Meropriyatie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    private String name;
    private Date data;
    //private String type;
    private String place;

    //private String level;
    @ManyToOne
    private LevelMeropriyatiya level;

    @ManyToOne
    private TypeMeropriyatiya type;

    private String otvetstvenniy;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "meropriyatie_id")
    private List<Dostijenie> dostList;


    public Meropriyatie(String name, Date data, TypeMeropriyatiya type, String place, LevelMeropriyatiya level, String otvetstvenniy) {
        this.name = name;
        this.data = data;
        this.type = type;
        this.place = place;
        this.level = level;
        this.otvetstvenniy = otvetstvenniy;
    }

    @Override
    public String toString() {
        return "Meropriyatie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", data=" + data +
                ", type='" + type.getName() + '\'' +
                ", place='" + place + '\'' +
                ", level='" + level.getName() + '\'' +
                ", otvetstvenniy='" + otvetstvenniy + '\'' +
                '}';
    }
}
