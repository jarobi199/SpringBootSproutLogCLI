package com.garden.service;

import com.garden.alert.AlertManager;
import com.garden.authentication.SessionContext;
import com.garden.enums.PlantStatus;
import com.garden.enums.Sun;
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
import java.util.Objects;
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

        if(validate(SessionContext.getUser(), bed, species, name, squareFeet, plantingDate)) {
            warnIfCrowded(bed, squareFeet);
            warnIfSunMismatch(bed, species.getSunNeed());

            Plant plant = new Plant(SessionContext.getUser().getId(), bed.getId(), species.getId(), species.getName(), name,
                    PlantStatus.PLANNED, plantingDate, squareFeet, plantingDate,
                    species.getDaysToMaturity(), species.getWateringIntervalDays(), species.isFrostTender(),
                    species.getExpectedYield(), species.getYieldUnit(), species.getSunNeed());
            plantRepository.save(plant);
        }
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
                plantTable.row(plant.getName(), plant.getSpeciesName(), optionalBed.isPresent() ? optionalBed.get().getName() : "N/A", plant.getPlantingDate().toString(), String.valueOf(plant.getSquareFeet()));
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
        warnIfCrowded(bed, plant.getSquareFeet());
        plant.setBedId(bed.getId());
        plantRepository.save(plant);
        System.out.println("This plant has been successfully moved to " + bed.getName() + ".");
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

    private void warnIfCrowded(Bed bed, int squareFeet) {
        if(isCrowded(bed, squareFeet)) {
            System.out.println(AlertManager.showWarning("This bed will be over capacity!"));
        }
    }

    private boolean isCrowded(Bed bed, int squareFeet) {
        return (bedService.squareFeetUsed(bed) + squareFeet) > bed.getAreaSqFt();
    }

    private void warnIfSunMismatch(Bed bed, Sun sunNeed) {
        if(!bed.getSunExposure().equals(sunNeed)) {
            System.out.println(AlertManager.showWarning("There is a sun exposure mismatch between the bed and the species!"));
        }
    }

    private boolean validate(User user, Bed bed, Species species, String name, int squareFeet, LocalDate plantingDate) {
        boolean result = true;

        Objects.requireNonNull(bed, "bed");
        Objects.requireNonNull(species, "species");
        Objects.requireNonNull(plantingDate, "plantingDate");

        // Free-text fields non-blank
        if (name == null || name.isBlank()) {
            result = false;
            System.out.println(AlertManager.showWarning("Plant name cannot be blank!"));
        }

        // Names unique per user within the plants collection
        String trimmed = name.trim();
        boolean duplicate = plantRepository.findByUserId(user.getId()).stream().anyMatch(p -> p.getName().equalsIgnoreCase(trimmed));
        if (duplicate) {
            result = false;
            System.out.println(AlertManager.showWarning("You already have a plant named \"" + trimmed + "\"."));
        }

        // Plant square feet >= 1
        if (squareFeet < 1) {
            result = false;
            System.out.println(AlertManager.showWarning("Square feet must be at least 1."));
        }

        // Adding a plant requires species.isHardyIn(hardinessZone)
        if (!species.isHardyIn(user.getHardinessZone())) {
            result = false;
            System.out.println(AlertManager.showWarning(species.getName() + " cannot survive in your zone (" + user.getHardinessZone() + ")."));
        }

        return result;
    }

}


