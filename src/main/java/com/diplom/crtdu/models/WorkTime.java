package com.diplom.crtdu.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class WorkTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String dayOfWeek;
    private String hour;

    @ManyToOne
    private Teacher teacher;

    public WorkTime(String dayOfWeek, String hour, Teacher teacher) {
        this.dayOfWeek = dayOfWeek;
        this.hour = hour;
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "WorkTime{" +
                "id=" + id +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", hour='" + hour + '\'' +
                ", teacher=" + teacher +
                '}';
    }
}
