package com.garden.menu;

import com.garden.enums.PlantStatus;
import com.garden.interfaces.IMenu;
import com.garden.model.*;
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
                case 3 -> viewPlantDetail();
                case 4 -> editPlant();
                case 5 -> markPlanted();
                case 6 -> movePlant();
                case 7 -> finishPlant();
                case 8 -> deletePlant();
            }
        }
        while (choice != 0);
    }

    public void deletePlant() {
        Plant plant = listPlantsAndSelect(null);
        System.out.println("Are you sure that you want to mark this plant as finished (Y/N)?:");
        boolean marked = InputHandler.getBooleanInput();

        if ((plant != null) && (marked)) {
            plantService.deletePlant(plant);
        }
    }

    public void editPlant() {
        Plant plant = listPlantsAndSelect(null);
        if (plant != null) {
            System.out.println("Enter the new name of the plant:");
            String plantName = InputHandler.getStringInput();
            System.out.println("Enter the new square feet of the plant:");
            int squareFeet = InputHandler.getIntegerInput();
            System.out.println("Enter the new water interval days:");
            int waterInterval = InputHandler.getIntegerInput();
            System.out.println("Enter the new expected yield separated by a comma (example: 5,10):");
            List<String> expectedYieldInputs = List.of(InputHandler.getStringInput().split(","));
            Range expectedYield = new Range(Integer.parseInt(expectedYieldInputs.getFirst()), Integer.parseInt(expectedYieldInputs.get(1)));
            System.out.println("Enter the days to maturity:");
            int daysToMaturity = InputHandler.getIntegerInput();

            plantService.editPlant(plant, plantName, squareFeet, waterInterval, expectedYield, daysToMaturity);
            System.out.println("The plant has been edited successfully!");
        }
    }

    public void viewPlantDetail() {
        Plant plant = listPlantsAndSelect(null);
        plantService.viewPlantDetail(plant);
    }

    public void movePlant() {
        Plant plant = listPlantsAndSelect(null);
        Bed bed = bedsMenu.listBedsAndSelect();

        if((bed !=null) && (plant != null)) {
            plantService.movePlant(plant, bed);
        }
    }

    public void finishPlant() {
        Plant plant = listPlantsAndSelect(null);
        System.out.println("Are you sure that you want to mark this plant as finished (Y/N)?:");
        boolean marked = InputHandler.getBooleanInput();

        if ((plant != null) && (marked)) {
            plantService.finishPlant(plant);
            System.out.println("The plant has been marked as finished!");
        }
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
