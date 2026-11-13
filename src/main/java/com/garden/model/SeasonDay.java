package com.garden.model;

import java.time.LocalDate;
import java.time.MonthDay;

public record SeasonDay(int month, int day) {

    public SeasonDay {
        MonthDay.of(month, day);
    }

    public LocalDate atYear(int year) {
        return MonthDay.of(month, day).atYear(year);
    }

}