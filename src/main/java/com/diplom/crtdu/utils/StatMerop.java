package com.diplom.crtdu.utils;

import java.util.Date;

public class StatMerop {
    private Long id;
    private String meropName;
    private Date meropDate;
    private String lvlName;
    private String typeName;
    private int total;
    private int first;
    private int second;
    private int third;

    public StatMerop() {
    }

    public StatMerop(Long id, String meropName, Date meropDate, String lvlName, String typeName, int total, int first, int second, int third) {
        this.id = id;
        this.meropName = meropName;
        this.meropDate = meropDate;
        this.lvlName = lvlName;
        this.typeName = typeName;
        this.total = total;
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeropName() {
        return meropName;
    }

    public void setMeropName(String meropName) {
        this.meropName = meropName;
    }

    public Date getMeropDate() {
        return meropDate;
    }

    public void setMeropDate(Date meropDate) {
        this.meropDate = meropDate;
    }

    public String getLvlName() {
        return lvlName;
    }

    public void setLvlName(String lvlName) {
        this.lvlName = lvlName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getThird() {
        return third;
    }

    public void setThird(int third) {
        this.third = third;
    }
}
