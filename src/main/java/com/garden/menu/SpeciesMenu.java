package com.garden.menu;

import com.garden.interfaces.IMenu;
import com.garden.model.Species;
import com.garden.service.SpeciesService;
import com.garden.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpeciesMenu implements IMenu {
    @Autowired
    private SpeciesService speciesService;

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
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }

    public Species listSpeciesAndSelect() {
        int number = 1;
        Species species = null;
        int choice;

        List<Species> speciesList = speciesService.getAllSpecies();

        if(!speciesList.isEmpty()) {
            for (Species s : speciesList) {
                System.out.println("[" + number + "] " +  s.getName()+ " (" + s.getCategory().name() + ")");
                number++;
            }
            System.out.println("Select a species:");
            choice = InputHandler.getIntegerInput();
            species = speciesList.get(choice - 1);
        }
        else
        {
            System.out.println("There are no beds available.");
        }

        return species;
    }


}
