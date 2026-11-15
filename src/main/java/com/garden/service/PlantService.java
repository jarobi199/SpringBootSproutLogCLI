package com.garden.service;

import com.garden.authentication.SessionContext;
import com.garden.enums.PlantStatus;
import com.garden.model.Bed;
import com.garden.model.Plant;
import com.garden.model.Species;
import com.garden.repository.BedRepository;
import com.garden.repository.PlantRepository;
import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PlantService {
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private BedService bedService;

    public void addPlant(Bed bed, Species species, String name, int squareFeet, LocalDate plantingDate) {

        Plant plant = new Plant(SessionContext.getUser().getId(), bed.getId(), species.getId(), species.getName(), name,
                PlantStatus.PLANNED, plantingDate, squareFeet, plantingDate,
                species.getDaysToMaturity(), species.getWateringIntervalDays(), species.isFrostTender(),
                species.getExpectedYield(), species.getYieldUnit(), species.getSunNeed());
        plantRepository.save(plant);
    }

    public List<Plant> findPlantsByBedId(String bedId) {
        return plantRepository.findByBedId(bedId);
    }

    public void listPlants() {
        List<Plant> plants = plantRepository.findByUserId(SessionContext.getUser().getId());
        if (!plants.isEmpty()) {
            System.out.println("| LIST OF PLANTS |");
            Table plantTable = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[blue, bold]NAME[/]",
                            "[blue, bold]SPECIES NAME[/]",
                            "[blue, bold]BED[/]",
                            "[blue, bold]PLANTING DATE[/]",
                            "[blue, bold]AREA SQUARE FEET[/]"
                    );
            plants.forEach(plant -> {
                Optional<Bed> optionalBed = bedRepository.findById(plant.getBedId());
                plantTable.row(plant.getName(), plant.getSpeciesName(), optionalBed.isPresent() ? optionalBed.get().getName() : "N/A", plant.getStatus().name(), String.valueOf(plant.getSquareFeet()));
                    });
            plantTable.render();
            System.out.println();
        }
    }

    public List<Plant> findPlantsByStatus(PlantStatus plantStatus) {
        List<Plant> plants = null;
        if(plantStatus == null) {
            plants = plantRepository.findByUserId(SessionContext.getUser().getId());
        }
        else {
            plants = plantRepository.findByUserIdAndStatus(SessionContext.getUser().getId(),  plantStatus);
        }

        return plants;
    }

    public void markPlanted(Plant plant, LocalDate plantingDate) {
        plant.setStatus(PlantStatus.GROWING);
        plant.setLastWatered(plantingDate);
        plantRepository.save(plant);
    }

    public void finishPlant(Plant plant) {
        plant.setStatus(PlantStatus.FINISHED);
        plantRepository.save(plant);
    }

    public void movePlant(Plant plant, Bed bed) {
        if(bedService.isSquareFeetAvailable(bed, plant.getSquareFeet())) {
            plant.setBedId(bed.getId());
            plantRepository.save(plant);
            System.out.println("This plant has been successfully moved to " + bed.getName() + ".");
        }
        else {
            System.out.println("This plant could not be moved to " + bed.getName() + " because there is not enough room.");
        }
    }

}
