package com.diplom.crtdu.utils;

public class ThreeIdsModel {
    private Long id1;
    private Long id2;
    private Long id3;

    public ThreeIdsModel(Long id1, Long id2, Long id3) {
        this.id1 = id1;
        this.id2 = id2;
        this.id3 = id3;
    }

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public Long getId3() {
        return id3;
    }

    public void setId3(Long id3) {
        this.id3 = id3;
    }

    public ThreeIdsModel() {
    }

    @Override
    public String toString() {
        return "ThreeIdsModel{" +
                "id1=" + id1 +
                ", id2=" + id2 +
                ", id3=" + id3 +
                '}';
    }
}
