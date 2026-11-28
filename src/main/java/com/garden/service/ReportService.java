package com.garden.service;

import com.garden.alert.AlertManager;
import com.garden.authentication.SessionContext;
import com.garden.model.*;
import com.garden.repository.BedRepository;
import com.garden.repository.HarvestRepository;
import com.garden.repository.PlantRepository;
import com.garden.repository.SpeciesRepository;
import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private SpeciesRepository speciesRepository  ;
    @Autowired
    private HarvestRepository harvestRepository;

    private static final int OPENING_SOON_DAYS = 14;

    public void plantNow() {
        User user = SessionContext.getUser();
        LocalDate today = LocalDate.now();
        LocalDate soon = today.plusDays(OPENING_SOON_DAYS);
        List<PlantableNow> results = new ArrayList<>();

        for (Species species : speciesRepository.findByUserId(user.getId())) {
            if (species.isHardyIn(user.getHardinessZone())) {
                PlantingWindow window = species.plantingWindow(user, today.getYear());
                if (!window.isEmpty()) {
                    if (window.isOpenOn(today)) {
                        results.add(new PlantableNow(species, window, true));
                    }
                    else if (window.opens().isAfter(today) && !window.opens().isAfter(soon)) {
                        results.add(new PlantableNow(species, window, false));      // opens within 14 days
                    }
                }

                // Open now first (closing soonest first), then opening soon (opening soonest first).
                results.sort(Comparator
                        .comparing((PlantableNow r) -> !r.openNow())
                        .thenComparing(r -> r.openNow() ? r.window().closes() : r.window().opens()));
            }
        }

        if (!results.isEmpty()) {
            Table plantingTable = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[blue, bold]SPECIES[/]",
                            "[blue, bold]OPEN DATE[/]",
                            "[blue, bold]CLOSE DAYE[/]",
                            "[blue, bold]NOTES[/]"
                    );
            for (PlantableNow plantableNow : results) {
                String notes = (plantableNow.openNow() ? "N/A" : "Planting window is opening in 14 days.");
                plantingTable.row(plantableNow.species().getName(), plantableNow.window().opens().toString(), plantableNow.window().closes().toString(), notes);
            }
            plantingTable.render();
        }
    }

    public void bedUtilization() {
        List<Bed> bedList = bedRepository.findByUserId(SessionContext.getUser().getId());
        if (!bedList.isEmpty()) {
            System.out.println("| BED UTILIZATION |");
            Table bedTable = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[blue, bold]BED NAME[/]",
                            "[blue, bold]SQUARE FEET USED[/]",
                            "[blue, bold]SQUARE FEET FREE[/]",
                            "[blue, bold]PERCENT USED[/]",
                            "[blue, bold]SUN EXPOSURE MISMATCH[/]"
                    );
            for (Bed bed : bedList) {
                int squareFeetUsed = plantRepository.findByBedId(bed.getId()).stream().mapToInt(Plant::getSquareFeet).sum();
                int squareFeetFree = bed.getAreaSqFt() -  squareFeetUsed;
                int percentUsed = (int) Math.round(((double) squareFeetUsed / bed.getAreaSqFt()) * 100);
                String mismatchWarnings = warnIfSunMismatch(bed, plantRepository.findByBedId(bed.getId()));
                bedTable.row(bed.getName(), String.valueOf(squareFeetUsed),  String.valueOf(squareFeetFree), percentUsed + "%", mismatchWarnings);
            }
            bedTable.render();
            System.out.println();
        }
        else
        {
            System.out.println(AlertManager.showWarning("There are no beds available."));
        }
    }

    private String warnIfSunMismatch(Bed bed, List<Plant> plantList) {
       StringBuilder stringBuilder = new StringBuilder();

       for (Plant plant : plantList) {
           if(!bed.getSunExposure().equals(plant.getSunNeed())) {
               stringBuilder.append(AlertManager.showWarning("There is a sun exposure mismatch between the bed and the species ")).append(plant.getSpeciesName()).append("!").append("\n");
           }
       }

        return (!stringBuilder.toString().isEmpty()) ? stringBuilder.toString() : stringBuilder.append("N/A").toString();
    }

    public void yieldSummary(int year) {
        List<Plant> plants = plantRepository.findByUserId(SessionContext.getUser().getId());
        if (!plants.isEmpty()) {
            System.out.println("| YIELD SUMMARY |");
            Table yieldTable = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[blue, bold]NAME[/]",
                            "[blue, bold]TOTAL[/]",
                            "[blue, bold]EXPECTED RANGE[/]",
                            "[blue, bold]RANGE POSITION[/]"
                    );
            //TODO: Add code here
            plants.forEach(plant -> {
                int totalYield = harvestRepository.findByPlantId(plant.getId()).stream().filter(harvest -> harvest.getDate().getYear() == year).mapToInt(Harvest::getQuantity).sum();
                Range expectedYield = plant.getExpectedYield();
                String rangePosition = expectedYield.getPositionDisplay(totalYield);
                yieldTable.row(plant.getName(), String.valueOf(totalYield), expectedYield.toString(), rangePosition);
            });

            yieldTable.render();
            System.out.println();
        }
    }

    public void plantingCalendar(int year) {
        User user = SessionContext.getUser();
        List<Species> speciesList = speciesRepository.findByUserId(user.getId())
                .stream().sorted(Comparator.comparing(s -> s.plantingWindow(user, year).opens())).toList();

        if(!speciesList.isEmpty()) {
            System.out.println("| PLANTING CALENDAR |");

            Table plantingCalendarTable = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[blue, bold]NAME[/]",
                            "[blue, bold]OPEN DATE[/]",
                            "[blue, bold]CLOSING DATE[/]"
                    );
            speciesList.forEach(species -> {
                PlantingWindow plantingWindow = species.plantingWindow(user, year);
                plantingCalendarTable.row(species.getName(), plantingWindow.opens().toString(), plantingWindow.closes().toString());
            });
            plantingCalendarTable.render();
        }
    }

}
