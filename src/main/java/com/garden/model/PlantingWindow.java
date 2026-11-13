package com.garden.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record PlantingWindow(LocalDate opens, LocalDate closes) {

    public boolean isEmpty() {
        return opens.isAfter(closes);
    }

    public boolean isOpenOn(LocalDate d) {
        return !isEmpty() && !d.isBefore(opens) && !d.isAfter(closes);
    }

    public long daysUntilClose(LocalDate d) {
        return ChronoUnit.DAYS.between(d, closes);
    }

}
