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
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private Date date;
    private String type;

    @Lob
    private byte[] content;

    @ManyToOne
    private Kid kid;

    public Document(String name, Date date, String type, byte[] content, Kid kid) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.content = content;
        this.kid = kid;
    }
}
