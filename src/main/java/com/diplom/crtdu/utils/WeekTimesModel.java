package com.diplom.crtdu.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;


@Getter
@Setter
@NoArgsConstructor
public class WeekTimesModel {
    private String key;
    private Boolean value;

    public WeekTimesModel(String key, Boolean value) {
        this.key = key;
        this.value = value;
    }
}
