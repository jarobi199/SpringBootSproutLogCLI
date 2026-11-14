package com.garden.menu;

import com.garden.interfaces.IMenu;
import com.garden.service.PlantService;
import com.garden.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlantsMenu implements IMenu {
    @Autowired
    private PlantService plantService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {

            }
        }
        while (choice != 0);
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

/*    public Bed listPlantsAndSelect() {
        int number = 1;
        Bed bed = null;
        int choice;

        List<Bed> beds = bedService.getAllBeds();

        if(!beds.isEmpty()) {
            for (Bed b : beds) {
                System.out.println("[" + number + "] " +  b.getName() + " (" + b.getBedType() + ")");
                number++;
            }
            System.out.println("Select a bottle:");
            choice = InputHandler.getIntegerInput();
            bed = beds.get(choice - 1);
        }
        else
        {
            System.out.println("There are no beds available.");
        }

        return bed;
    }*/

}
