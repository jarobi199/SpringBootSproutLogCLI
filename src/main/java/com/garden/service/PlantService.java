package com.garden.service;

import com.garden.authentication.SessionContext;
import com.garden.enums.PlantStatus;
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
import java.util.List;
import java.util.Optional;

@Service
public class PlantService {
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private HarvestRepository harvestRepository;
    @Autowired
    private BedService bedService;
    @Autowired
    private TaskService taskService;

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
        List<Plant> plants;
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

    public void viewPlantDetail(Plant plant) {
        Species species = speciesRepository.findById(plant.getSpeciesId()).orElse(null);
        System.out.println(plant.getDetails());
        System.out.println("Season Yield: " + getSeasonYield(plant) + " " + plant.getYieldUnit());
        if (species != null) {
            System.out.println("Planting Window: " + species.plantingWindow(SessionContext.getUser(), plant.getPlantingDate().getYear()).opens()
                    + " - " + species.plantingWindow(SessionContext.getUser(), plant.getPlantingDate().getYear()).closes());
        }

        List<Task> tasks = taskService.findAllTasksByPlant(plant);
        if (tasks.isEmpty()) {
            System.out.println("No tasks found");
        }
        else
        {
           taskService.displayAllTasks(tasks);
        }
    }

    private int getSeasonYield(Plant plant) {
        int currentYear = LocalDate.now().getYear();
        return harvestRepository.findByPlantId(plant.getId()).stream().filter(harvest -> harvest.getDate().getYear() == currentYear).mapToInt(Harvest::getQuantity).sum();
    }


    public void editPlant(Plant plant, String plantName, int squareFeet, int waterInterval, Range expectedYield, int daysToMaturity) {
        plant.setName(plantName);
        plant.setSquareFeet(squareFeet);
        plant.setWateringIntervalDays(waterInterval);
        plant.setExpectedYield(expectedYield);
        plant.setDaysToMaturity(daysToMaturity);
        plantRepository.save(plant);
    }

    public void deletePlant(Plant plant) {
        if(validDelete(plant)) {
            plantRepository.delete(plant);
        }
        else
        {
            System.out.println("This plant could not be deleted.");
        }
    }

    private boolean validDelete(Plant plant) {
        int taskCount = taskService.findAllTasksByPlant(plant).size();
        int harvestCount = harvestRepository.findByPlantId(plant.getId()).size();

        return (harvestCount == 0) &&  (taskCount == 0);
    }

}
