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
    private String type;
    private String place;

    private String level;

    private String otvetstvenniy;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "meropriyatie_id")
    private List<Dostijenie> dostList;


    public Meropriyatie(String name, Date data, String type, String place) {
        this.name = name;
        this.data = data;
        this.type = type;
        this.place = place;
    }

    @Override
    public String toString() {
        return "Meropriyatiye{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", data=" + data +
                ", type='" + type + '\'' +
                ", place='" + place + '\'' +
                '}';
    }
}
