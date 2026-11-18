package com.garden.menu;

import com.garden.enums.PlantStatus;
import com.garden.interfaces.IMenu;
import com.garden.model.Harvest;
import com.garden.model.Plant;
import com.garden.service.HarvestService;
import com.garden.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class HarvestMenu implements IMenu {
    @Autowired
    private HarvestService harvestService;
    @Autowired
    private PlantsMenu plantsMenu;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> logHarvest();
                case 2 -> historyByPlant();
                case 3 -> deleteHarvest();
            }
        }
        while (choice != 0);
    }

    public void historyByPlant() {
        Plant plant = plantsMenu.listPlantsAndSelect(null);
        if (plant != null) {
            harvestService.historyByPlant(plant);
        }

    }

    public void deleteHarvest() {
        Harvest harvest = listHarvestAndSelect();
        if (harvest != null) {
            harvestService.deleteHarvest(harvest);
            System.out.println("The harvest has been successfully deleted!");
        }
    }

    public void logHarvest() {
        Plant plant = plantsMenu.listPlantsAndSelect(PlantStatus.GROWING);
        if (plant != null) {
            System.out.println("Enter the harvest date (YYYY-MM-DD):");
            LocalDate harvestDate = InputHandler.getDateInput();
            System.out.println("Enter the quantity:");
            int quantity = InputHandler.getIntegerInput();
            System.out.println("Enter the notes:");
            String notes = InputHandler.getStringInput();

            harvestService.logHarvest(plant, harvestDate, quantity, notes);
            System.out.println("The harvest has been successfully logged!");
        }

    }

    public void printOptions() {
        System.out.println();
        System.out.println("[1] Log harvest");
        System.out.println("[2] History by plant");
        System.out.println("[3] Delete harvest");
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }

    public Harvest listHarvestAndSelect() {
        int number = 1;
        Harvest harvest = null;
        int choice;

        List<Harvest> harvests = harvestService.findAllHarvests();

        if(!harvests.isEmpty()) {
            for (Harvest h : harvests) {
                System.out.println("[" + number + "] " +  h.getPlantName() + " (" + h.getSpeciesName() + ")");
                number++;
            }
            System.out.println("Select a harvest:");
            choice = InputHandler.getIntegerInput();
            harvest = harvests.get(choice - 1);
        }
        else
        {
            System.out.println("There are no harvests available.");
        }

        return harvest;
    }
}
