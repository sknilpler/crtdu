package com.diplom.crtdu.utils;

import com.diplom.crtdu.models.Meropriyatie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DayMerop {
    private int date;
    private List<Meropriyatie> events;

    public DayMerop(int date, List<Meropriyatie> events) {
        this.date = date;
        this.events = events;
    }

    public DayMerop(int date) {
        this.date = date;
    }
}
