package com.garden.service;

import com.garden.authentication.SessionContext;
import com.garden.enums.SpeciesCategory;
import com.garden.enums.Sun;
import com.garden.enums.YieldUnit;
import com.garden.factory.SpeciesFactory;
import com.garden.model.PlantingWindow;
import com.garden.model.Range;
import com.garden.model.Species;
import com.garden.model.User;
import com.garden.repository.PlantRepository;
import com.garden.repository.SpeciesRepository;
import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SpeciesService {
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private PlantRepository plantRepository;

    public List<Species> getAllSpecies() {
        return speciesRepository.findByUserId(SessionContext.getUser().getId());
    }

    public void addSpecies(SpeciesCategory speciesCategory, String name, Sun sun, int maturityDays, int wateringInterval, boolean frostTender, Range windowOffsets,
                           Range expectedYield, YieldUnit yieldUnit, int minHardinessZone) {
        Species species = SpeciesFactory.createSpecies(speciesCategory, name, sun, maturityDays, wateringInterval, frostTender, windowOffsets, expectedYield, yieldUnit, minHardinessZone);
        speciesRepository.save(species);
    }

    public void listSpecies() {
        List<Species> speciesList = speciesRepository.findByUserId(SessionContext.getUser().getId()); //TODO: Sort
        if (!speciesList.isEmpty()) {
            System.out.println("| SPECIES |");
            Table speciesTable = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[blue, bold]NAME[/]",
                            "[blue, bold]CATEGORY[/]",
                            "[blue, bold]SUN NEED[/]",
                            "[blue, bold]DAYS TO MATURITY[/]",
                            "[blue, bold]WATERING INTERVAL[/]",
                            "[blue, bold]WINDO OFFSETS[/]",
                            "[blue, bold]EXPECTED YIELD[/]"
                    );
            speciesList.forEach(species -> {
                speciesTable.row(species.getName(), species.getCategory().name(), species.getSunNeed().name(),
                        String.valueOf(species.getDaysToMaturity()), String.valueOf(species.getWateringIntervalDays()), species.getWindowOffsets().toString(), species.getExpectedYield().toString());
            });
            speciesTable.render();
            System.out.println();
        }
    }

    public void viewSpeciesDetail(Species species) {
        int totalPlants = plantRepository.findBySpeciesId(species.getId()).size();

        System.out.println("| SPECIES DETAIL |");
        System.out.println(species.getDetails());
        System.out.println("Planning Window: " + describeWindow(species, SessionContext.getUser()));
        System.out.println("Total Number Of Plants Using Species: " + totalPlants);
    }

    private String describeWindow(Species species, User user) {
        String window = null;
        int year = LocalDate.now().getYear();
        PlantingWindow plantingWindow = species.plantingWindow(user, year);

        if (!plantingWindow.isEmpty()) {
            window = plantingWindow.opens() + " - " + plantingWindow.closes();
        }
        else
        {
            window = "There is no planting window for this season";
        }

        return window;
    }

    public void deleteSpecies(Species species) {
        int plantCount = plantRepository.findBySpeciesId(species.getId()).size();
        if (plantCount > 0) {
            System.out.println("This species cannot be deleted because there are plants that reference it.");
        }
        else
        {
            speciesRepository.delete(species);
            System.out.println("The species has been successfully deleted!");
        }

    }
}

