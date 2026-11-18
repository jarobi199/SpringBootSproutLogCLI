package com.garden.menu;

import com.garden.enums.PlantStatus;
import com.garden.enums.TaskType;
import com.garden.interfaces.IMenu;
import com.garden.model.Harvest;
import com.garden.model.Plant;
import com.garden.service.TaskService;
import com.garden.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class CareMenu implements IMenu {
    @Autowired
    private TaskService taskService;
    @Autowired
    private PlantsMenu plantsMenu;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> logTask();
                case 2 -> historyByPlant();
                case 3 -> careSchedule();
            }
        }
        while (choice != 0);
    }

    public void careSchedule() {
        taskService.displayCareSchedule();
    }

    public void historyByPlant() {
        Plant plant = plantsMenu.listPlantsAndSelect(null);
        if (plant != null) {
           taskService.historyByPlant(plant);
        }
    }

    public void logTask() {
        Plant plant = plantsMenu.listPlantsAndSelect(PlantStatus.GROWING);
        if (plant != null) {
            System.out.println("Enter the task type ( WATER, FERTILIZE, PRUNE, MULCH, WEED, PEST_CONTROL, TRANSPLANT, OTHER):");
            TaskType taskType = TaskType.valueOf(InputHandler.getStringInput().toUpperCase());
            System.out.println("Enter the date (YYYY-MM-DD):");
            LocalDate date = InputHandler.getDateInput();
            System.out.println("Enter the notes:");
            String notes = InputHandler.getStringInput();

            taskService.logTask(plant, taskType, date, notes);
            System.out.println("The task has been logged successfully!");
        }
    }

    public void printOptions() {
        System.out.println();
        System.out.println("[1] Log task");
        System.out.println("[2] History by plant");
        System.out.println("[3] Care schedule");
        System.out.println("[3] Delete task");
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }

    /*public Harvest listHarvestAndSelect() {
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
    }*/
}
