package com.garden.alert;

import com.garden.enums.AlertSeverity;
import com.garden.enums.AlertType;
import com.garden.enums.PlantStatus;
import com.garden.interfaces.AlertStrategy;
import com.garden.model.Plant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class WateringDueStrategy implements AlertStrategy {

    @Value("${sproutlog.watering-critical-overdue-days}")
    private int criticalOverdueDays;

    @Override
    public List<AlertResult> evaluate(AlertContext ctx) {
        LocalDate today = LocalDate.now();
        int leadDays = ctx.user().getWateringLeadDays();
        List<AlertResult> results = new ArrayList<>();

        for (Plant plant : ctx.plants()) {
            if (plant.getStatus().equals(PlantStatus.GROWING)) {
                LocalDate due = plant.getLastWatered().plusDays(plant.getWateringIntervalDays());
                long daysUntilDue = ChronoUnit.DAYS.between(today, due);
                if (daysUntilDue <= leadDays) {
                    long overdue = -daysUntilDue;
                    AlertSeverity severity = (daysUntilDue < 0 && overdue >= criticalOverdueDays)
                            ? AlertSeverity.CRITICAL : AlertSeverity.WARNING;

                    results.add(new AlertResult(plant.getName(), AlertType.WATERING_DUE, severity, message(daysUntilDue)));
                }
            }
        }

        return results;
    }

    private String message(long daysUntilDue) {
        if (daysUntilDue < 0) {
            long n = -daysUntilDue;
            return "overdue by " + n + (n == 1 ? " day" : " days");
        }
        if (daysUntilDue == 0) return "due today";
        if (daysUntilDue == 1) return "due tomorrow";
        return "due in " + daysUntilDue + " days";
    }
}