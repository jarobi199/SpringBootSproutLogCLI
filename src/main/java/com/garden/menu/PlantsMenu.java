package com.garden.menu;

import com.garden.enums.PlantStatus;
import com.garden.interfaces.IMenu;
import com.garden.model.Bed;
import com.garden.model.Plant;
import com.garden.model.Species;
import com.garden.service.PlantService;
import com.garden.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PlantsMenu implements IMenu {
    @Autowired
    private PlantService plantService;
    @Autowired
    private BedsMenu bedsMenu;
    @Autowired
    private SpeciesMenu speciesMenu;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> listPlants();
                case 2 -> addPlant();
                case 5 -> markPlanted();
            }
        }
        while (choice != 0);
    }

    public void markPlanted() {
        Plant plant = listPlantsAndSelect(PlantStatus.PLANNED);
        if(plant != null) {
            System.out.println("Enter the planting date (YYYY-MM-DD):");
            LocalDate plantingDate = InputHandler.getDateInput();

            plantService.markPlanted(plant, plantingDate);
            System.out.println("Plant marked successfully!");
        }
    }

    public void listPlants() {
        plantService.listPlants();
    }

    public void addPlant() {
        Bed bed = bedsMenu.listBedsAndSelect();
        Species species = speciesMenu.listSpeciesAndSelect();

        System.out.println("Enter the name of the plant:");
        String plantName = InputHandler.getStringInput();
        System.out.println("Enter the square feet of the plant:");
        int squareFeet = InputHandler.getIntegerInput();
        System.out.println("Enter the planting date (YYYY-MM-DD):");
        LocalDate plantingDate = InputHandler.getDateInput();

        plantService.addPlant(bed, species, plantName, squareFeet, plantingDate);
        System.out.println("The plant has been added successfully!");
    }


    public void printOptions() {
        System.out.println();
        System.out.println("[1] List plants");
        System.out.println("[2] Add plant");
        System.out.println("[3] View plant detail");
        System.out.println("[4] Edit plant");
        System.out.println("[5] Mark planted");
        System.out.println("[6] Move plant");
        System.out.println("[7] Finish plant");
        System.out.println("[8] Delete plant");
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }

    public Plant listPlantsAndSelect(PlantStatus status) {
        int number = 1;
        Plant plant = null;
        int choice;

        List<Plant> plants = plantService.findPlantsByStatus(status);

        if(!plants.isEmpty()) {
            for (Plant p : plants) {
                System.out.println("[" + number + "] " +  p.getName() + " (" + p.getSpeciesName() + ")");
                number++;
            }
            System.out.println("Select a plant:");
            choice = InputHandler.getIntegerInput();
            plant = plants.get(choice - 1);
        }
        else
        {
            System.out.println("There are no plants available.");
        }

        return plant;
    }
}
