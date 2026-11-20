package com.garden.alert;

import com.garden.interfaces.AlertStrategy;
import com.garden.model.Plant;
import com.garden.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class FrostRiskStrategy implements AlertStrategy {

    private static final DateTimeFormatter MONTH_DAY =
            DateTimeFormatter.ofPattern("MMM d", Locale.US);

    @Override
    public List<AlertResult> evaluate(AlertContext ctx) {
        User user = ctx.getUser();
        LocalDate today = LocalDate.now();
        LocalDate lastFrost  = user.getLastSpringFrost().atYear(today.getYear());
        LocalDate firstFrost = user.getFirstFallFrost().atYear(today.getYear());
        List<AlertResult> results = new ArrayList<>();

        for (Plant plant : ctx.getPlants()) {
            if (plant.getStatus() != PlantStatus.GROWING || !plant.isFrostTender()) continue;

            if (!today.isAfter(lastFrost)) {
                results.add(result(plant, AlertSeverity.WARNING,
                        "frost still possible until " + lastFrost.format(MONTH_DAY)));
                continue;
            }

            long daysUntilFrost = ChronoUnit.DAYS.between(today, firstFrost);
            if (daysUntilFrost <= 0) {
                results.add(result(plant, AlertSeverity.CRITICAL,
                        "past the average first frost"));
            } else if (daysUntilFrost <= user.getFrostWarningDays()) {
                results.add(result(plant, AlertSeverity.WARNING,
                        "first frost expected in " + daysUntilFrost
                                + (daysUntilFrost == 1 ? " day" : " days")));
            }
        }
        return results;
    }

    private AlertResult result(Plant plant, AlertSeverity severity, String message) {
        return new AlertResult(plant.getName(), AlertType.FROST_RISK, severity, message);
    }

}