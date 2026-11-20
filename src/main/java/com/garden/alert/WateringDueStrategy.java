package com.garden.alert;

import org.springframework.stereotype.Component;

@Component
public class WateringDueStrategy implements AlertStrategy {

    private final int criticalOverdueDays;

    public WateringDueStrategy(
            @Value("${sproutlog.watering-critical-overdue-days}") int criticalOverdueDays) {
        this.criticalOverdueDays = criticalOverdueDays;
    }

    @Override
    public List<AlertResult> evaluate(AlertContext ctx) {
        LocalDate today = LocalDate.now();
        int leadDays = ctx.getUser().getWateringLeadDays();
        List<AlertResult> results = new ArrayList<>();

        for (Plant plant : ctx.getPlants()) {
            if (plant.getStatus() != PlantStatus.GROWING) continue;

            LocalDate due = plant.getLastWatered().plusDays(plant.getWateringIntervalDays());
            long daysUntilDue = ChronoUnit.DAYS.between(today, due);
            if (daysUntilDue > leadDays) continue;

            long overdue = -daysUntilDue;
            AlertSeverity severity = (daysUntilDue < 0 && overdue >= criticalOverdueDays)
                    ? AlertSeverity.CRITICAL : AlertSeverity.WARNING;

            results.add(new AlertResult(plant.getName(), AlertType.WATERING_DUE,
                    severity, message(daysUntilDue)));
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