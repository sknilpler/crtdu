package com.diplom.crtdu.utils;

public class StatKids {
    private String caName;
    private String pdo;
    private int total;
    private int m;
    private int f;
    private int rf;
    private int rk;
    private int o;

   public StatKids() {
    }

    public StatKids(String caName, String pdo, int total, int m, int f, int rf, int rk, int o) {
        this.caName = caName;
        this.pdo = pdo;
        this.total = total;
        this.m = m;
        this.f = f;
        this.rf = rf;
        this.rk = rk;
        this.o = o;
    }

    public String getCaName() {
        return caName;
    }

    public void setCaName(String caName) {
        this.caName = caName;
    }

    public String getPdo() {
        return pdo;
    }

    public void setPdo(String pdo) {
        this.pdo = pdo;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getRf() {
        return rf;
    }

    public void setRf(int rf) {
        this.rf = rf;
    }

    public int getRk() {
        return rk;
    }

    public void setRk(int rk) {
        this.rk = rk;
    }


    public int getO() {
        return o;
    }

    public void setO(int o) {
        this.o = o;
    }

    @Override
    public String toString() {
        return "StatKids{" +
                "caName='" + caName + '\'' +
                ", pdo='" + pdo + '\'' +
                ", total=" + total +
                ", m=" + m +
                ", f=" + f +
                ", rf=" + rf +
                ", rk=" + rk +
                ", o=" + o +
                '}';
    }
}
