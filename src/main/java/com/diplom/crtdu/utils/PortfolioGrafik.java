package com.diplom.crtdu.utils;

import java.util.List;

public class PortfolioGrafik {
    private List<String> dates;
    private List<String> subjects;
    private List<Integer>statBal;

    public PortfolioGrafik(List<String> dates, List<String> subjects, List<Integer> statBal) {
        this.dates = dates;
        this.subjects = subjects;
        this.statBal = statBal;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<Integer> getStatBal() {
        return statBal;
    }

    public void setStatBal(List<Integer> statBal) {
        this.statBal = statBal;
    }
}
