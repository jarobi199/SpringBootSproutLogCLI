package com.garden.service;

import com.garden.alert.AlertContext;
import com.garden.alert.AlertManager;
import com.garden.alert.AlertResult;
import com.garden.authentication.SessionContext;
import com.garden.enums.AlertType;
import com.garden.enums.PlantStatus;
import com.garden.model.Plant;
import com.garden.model.Species;
import com.garden.repository.PlantRepository;
import com.garden.repository.SpeciesRepository;
import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AlertService {
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private SpeciesRepository speciesRepository;

    public void displayAlerts(AlertType alertType) {
        AlertContext alertContext = getAlertContext();
        List<AlertResult> results = alertManager.evaluate(alertContext);
        if(alertType != null) {
            results = results.stream().filter(result -> result.type().equals(alertType)).toList();
        }
        displayAlertResults(results);
    }

    private AlertContext getAlertContext() {
        List<Plant> plants = plantRepository.findByUserId(SessionContext.getUser().getId()).stream().filter(plant -> !PlantStatus.FINISHED.equals(plant.getStatus())).toList();
        Map<String, Species> speciesMap = speciesRepository.findByUserId(SessionContext.getUser().getId()).stream()
                .collect(Collectors.toMap(Species::getId, Function.identity()));

        return new AlertContext(SessionContext.getUser(), plants, speciesMap);
    }

    public void displayAlertResults(List<AlertResult> alertResults) {
        if(alertResults.isEmpty()) {
            System.out.println("No alerts found.\n");
        }
        else
        {
            System.out.println("| ALERTS |");
            Table table = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[*blue, bold]SUBJECT[/]",
                            "[*blue, bold]ALERT TYPE[/]",
                            "[*blue, bold]SEVERITY[/]",
                            "[*blue, bold]MESSAGE[/]"
                    );
            for (AlertResult alertResult : alertResults) {
                table.row(alertResult.subject(), alertResult.type().name(),  alertResult.severity().name(), alertResult.message());
            }
            table.render();
        }
    }


}
