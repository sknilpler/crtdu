package com.diplom.crtdu.utils;

import com.diplom.crtdu.models.Krujok;

public class KrujokPosechenie {
    private long id;
    private String name;
    private int percent;

    public KrujokPosechenie() {
    }

    public KrujokPosechenie(long id, String name, int percent) {
        this.id = id;
        this.name = name;
        this.percent = percent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
