package com.diplom.crtdu.utils;

import com.diplom.crtdu.models.CreativeAssociation;
import com.diplom.crtdu.models.Kid;
import com.diplom.crtdu.models.Krujok;

public class KidsOnMeropModel {
    private CreativeAssociation creativeAssociation;
    private Krujok krujok;
    private Kid kid;

    public KidsOnMeropModel() {
    }

    public KidsOnMeropModel(CreativeAssociation creativeAssociation, Krujok krujok, Kid kid) {
        this.creativeAssociation = creativeAssociation;
        this.krujok = krujok;
        this.kid = kid;
    }

    public CreativeAssociation getCreativeAssociation() {
        return creativeAssociation;
    }

    public void setCreativeAssociation(CreativeAssociation creativeAssociation) {
        this.creativeAssociation = creativeAssociation;
    }

    public Krujok getKrujok() {
        return krujok;
    }

    public void setKrujok(Krujok krujok) {
        this.krujok = krujok;
    }

    public Kid getKid() {
        return kid;
    }

    public void setKid(Kid kid) {
        this.kid = kid;
    }

    @Override
    public String toString() {
        return "KidsOnMeropModel{" +
                "creativeAssociation=" + creativeAssociation +
                ", krujok=" + krujok +
                ", kid=" + kid +
                '}';
    }
}
