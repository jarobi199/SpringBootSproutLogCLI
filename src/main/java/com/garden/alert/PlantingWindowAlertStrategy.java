package com.garden.alert;

import com.garden.enums.AlertSeverity;
import com.garden.enums.AlertType;
import com.garden.enums.PlantStatus;
import com.garden.interfaces.AlertStrategy;
import com.garden.model.Plant;
import com.garden.model.PlantingWindow;
import com.garden.model.Species;
import com.garden.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class PlantingWindowAlertStrategy implements AlertStrategy {

    private static final DateTimeFormatter MONTH_DAY =
            DateTimeFormatter.ofPattern("MMM d", Locale.US);

    @Override
    public List<AlertResult> evaluate(AlertContext ctx) {
        User user = ctx.user();
        LocalDate today = LocalDate.now();
        List<AlertResult> results = new ArrayList<>();

        for (Plant plant : ctx.plants()) {
            if (plant.getStatus() != PlantStatus.PLANNED) continue;

            Species species = ctx.speciesFor(plant);
            // Judge against the planned planting year, not today's year.
            PlantingWindow window =
                    species.plantingWindow(user, plant.getPlantingDate().getYear());

            if (window.isEmpty()) {
                results.add(result(plant, AlertSeverity.CRITICAL,
                        "no valid planting window this year"));
                continue;
            }

            long daysUntilClose = window.daysUntilClose(today);
            if (daysUntilClose < 0) {
                results.add(result(plant, AlertSeverity.CRITICAL,
                        "window closed " + (-daysUntilClose) + " days ago (closed "
                                + window.closes().format(MONTH_DAY) + ")"));
            } else if (daysUntilClose <= user.getPlantingLeadDays()) {
                results.add(result(plant, AlertSeverity.WARNING,
                        "window closes in " + daysUntilClose + " days"));
            }
            // Otherwise the window is still opening or comfortably open: no alert.
        }
        return results;
    }

    private AlertResult result(Plant plant, AlertSeverity severity, String message) {
        return new AlertResult(plant.getName(), AlertType.PLANTING_WINDOW, severity, message);
    }
}