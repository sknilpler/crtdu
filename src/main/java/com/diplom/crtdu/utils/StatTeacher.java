package com.diplom.crtdu.utils;

public class StatTeacher {
    private String fio;
    private int numMerop;
    private int numTopMerop;
    private int numFirst;
    private int numSecond;
    private int numThird;
    private int numKids;

    public StatTeacher(String fio, int numMerop, int numTopMerop, int numFirst, int numSecond, int numThird, int numKids) {
        this.fio = fio;
        this.numMerop = numMerop;
        this.numTopMerop = numTopMerop;
        this.numFirst = numFirst;
        this.numSecond = numSecond;
        this.numThird = numThird;
        this.numKids = numKids;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public int getNumMerop() {
        return numMerop;
    }

    public void setNumMerop(int numMerop) {
        this.numMerop = numMerop;
    }

    public int getNumTopMerop() {
        return numTopMerop;
    }

    public void setNumTopMerop(int numTopMerop) {
        this.numTopMerop = numTopMerop;
    }

    public int getNumFirst() {
        return numFirst;
    }

    public void setNumFirst(int numFirst) {
        this.numFirst = numFirst;
    }

    public int getNumSecond() {
        return numSecond;
    }

    public void setNumSecond(int numSecond) {
        this.numSecond = numSecond;
    }

    public int getNumThird() {
        return numThird;
    }

    public void setNumThird(int numThird) {
        this.numThird = numThird;
    }

    public int getNumKids() {
        return numKids;
    }

    public void setNumKids(int numKids) {
        this.numKids = numKids;
    }

    @Override
    public String toString() {
        return "StatTeacher{" +
                "fio='" + fio + '\'' +
                ", numMerop=" + numMerop +
                ", numTopMerop=" + numTopMerop +
                ", numFirst=" + numFirst +
                ", numSecond=" + numSecond +
                ", numThird=" + numThird +
                ", numKids=" + numKids +
                '}';
    }
}
