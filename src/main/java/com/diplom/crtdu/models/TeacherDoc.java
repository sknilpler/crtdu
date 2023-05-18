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
public class TeacherDoc {

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
    private Teacher teacher;

    public TeacherDoc(String name, Date date, String type, byte[] content, Teacher teacher) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.content = content;
        this.teacher = teacher;
    }
}
