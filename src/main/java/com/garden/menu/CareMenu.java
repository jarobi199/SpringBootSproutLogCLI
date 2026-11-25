package com.garden.menu;

import com.garden.enums.PlantStatus;
import com.garden.enums.TaskType;
import com.garden.interfaces.IMenu;
import com.garden.model.Plant;
import com.garden.model.Task;
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
                case 4 -> deleteTask();
            }
        }
        while (choice != 0);
    }

    public void deleteTask() {
        Task task = listTasksAndSelect();
        if (task != null) {
            System.out.println("Are you sure you want to delete this task? (Y/N):");
            boolean answer = InputHandler.getBooleanInput();
            if(answer) {
                taskService.deleteTask(task);
                System.out.println("The task has been successfully deleted!");
            }
        }
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
        System.out.println("[4] Delete task");
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }

    public Task listTasksAndSelect() {
        int number = 1;
        Task task = null;
        int choice;

        List<Task> tasks = taskService.findAllTasks();

        if(!tasks.isEmpty()) {
            for (Task t : tasks) {
                System.out.println("[" + number + "] " +  t.getNotes()+ " (" + t.getPlantName() + " - " + t.getDate() + " )");
                number++;
            }
            System.out.println("Select a task:");
            choice = InputHandler.getIntegerInput();
            task = tasks.get(choice - 1);
        }
        else
        {
            System.out.println("There are no tasks available.");
        }

        return task;
    }
}
