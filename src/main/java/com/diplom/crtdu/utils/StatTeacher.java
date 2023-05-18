package com.diplom.crtdu.utils;

public class StatTeacher {
    private String fio;
    private int numMerop;
    private int numTopMerop;
    private int numRajonMerop;
    private int numCityMerop;
    private int numRegMerop;
    private int numNacMerop;
    private int numFirst;
    private int numSecond;
    private int numThird;
    private int numKids;
    private int numKrujki;


    public StatTeacher(String fio, int numMerop, int numTopMerop, int numRajonMerop, int numCityMerop, int numRegMerop, int numNacMerop, int numFirst, int numSecond, int numThird, int numKids, int numKrujki) {
        this.fio = fio;
        this.numMerop = numMerop;
        this.numTopMerop = numTopMerop;
        this.numRajonMerop = numRajonMerop;
        this.numCityMerop = numCityMerop;
        this.numRegMerop = numRegMerop;
        this.numNacMerop = numNacMerop;
        this.numFirst = numFirst;
        this.numSecond = numSecond;
        this.numThird = numThird;
        this.numKids = numKids;
        this.numKrujki = numKrujki;
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

    public int getNumKrujki() {
        return numKrujki;
    }

    public void setNumKrujki(int numKrujki) {
        this.numKrujki = numKrujki;
    }

    public int getNumRajonMerop() {
        return numRajonMerop;
    }

    public void setNumRajonMerop(int numRajonMerop) {
        this.numRajonMerop = numRajonMerop;
    }

    public int getNumCityMerop() {
        return numCityMerop;
    }

    public void setNumCityMerop(int numCityMerop) {
        this.numCityMerop = numCityMerop;
    }

    public int getNumRegMerop() {
        return numRegMerop;
    }

    public void setNumRegMerop(int numRegMerop) {
        this.numRegMerop = numRegMerop;
    }

    public int getNumNacMerop() {
        return numNacMerop;
    }

    public void setNumNacMerop(int numNacMerop) {
        this.numNacMerop = numNacMerop;
    }

    @Override
    public String toString() {
        return "StatTeacher{" +
                "fio='" + fio + '\'' +
                ", numMerop=" + numMerop +
                ", numTopMerop=" + numTopMerop +
                ", numRajonMerop=" + numRajonMerop +
                ", numCityMerop=" + numCityMerop +
                ", numRegMerop=" + numRegMerop +
                ", numNacMerop=" + numNacMerop +
                ", numFirst=" + numFirst +
                ", numSecond=" + numSecond +
                ", numThird=" + numThird +
                ", numKids=" + numKids +
                ", numKrujki=" + numKrujki +
                '}';
    }
}
