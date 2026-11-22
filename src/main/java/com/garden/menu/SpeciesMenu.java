package com.garden.menu;

import com.garden.enums.SpeciesCategory;
import com.garden.enums.Sun;
import com.garden.enums.YieldUnit;
import com.garden.interfaces.IMenu;
import com.garden.model.Range;
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
                case 1 -> listSpecies();
                case 2 -> addSpecies();
                case 3 -> viewSpeciesDetail();
            }
        }
        while (choice != 0);
    }

    public void viewSpeciesDetail() {
        Species species = listSpeciesAndSelect();
        if (species != null) {
            speciesService.viewSpeciesDetail(species);
        }
    }

    public void listSpecies() {
        speciesService.listSpecies();
    }

    public void addSpecies() {
        System.out.println("Enter the species category (WARM_SEASON, COOL_SEASON, PERENNIAL):");
        SpeciesCategory speciesCategory = SpeciesCategory.valueOf(InputHandler.getStringInput().toUpperCase());
        System.out.println("Enter the name:");
        String name = InputHandler.getStringInput();
        System.out.println("Enter the sun need (SHADE, PART_SUN, FULL_SUN):");
        Sun sun = Sun.valueOf(InputHandler.getStringInput().toUpperCase());
        System.out.println("Enter the days to maturity:");
        int maturityDays = InputHandler.getIntegerInput();
        System.out.println("Enter the watering interval:");
        int wateringInterval = InputHandler.getIntegerInput();
        System.out.println("Is this a frost tender? (Y/N):");
        boolean frostTender = InputHandler.getBooleanInput();
        System.out.println("Enter the window offsets separated by a comma (example: 5,10):");
        List<String> windowOffsetInput = List.of(InputHandler.getStringInput().split(","));
        Range windowOffsets = new Range(Integer.parseInt(windowOffsetInput.getFirst()), Integer.parseInt(windowOffsetInput.get(1)));
        System.out.println("Enter the expected yield separated by a comma (example: 6,11):");
        List<String> expectedYieldInputs = List.of(InputHandler.getStringInput().split(","));
        Range expectedYield = new Range(Integer.parseInt(expectedYieldInputs.getFirst()), Integer.parseInt(expectedYieldInputs.get(1)));
        System.out.println("Enter the yield unit (OUNCES, COUNT):");
        YieldUnit yieldUnit = YieldUnit.valueOf(InputHandler.getStringInput().toUpperCase());
        int minHardinessZone = 0;

        if(SpeciesCategory.PERENNIAL.equals(speciesCategory)) {
            System.out.print("Enter the minimum hardiness zone:");
            minHardinessZone = InputHandler.getIntegerInput();
        }

        speciesService.addSpecies(speciesCategory, name, sun, maturityDays, wateringInterval, frostTender, windowOffsets, expectedYield, yieldUnit, minHardinessZone);
        System.out.println("The species has been successfully added!");
    }

    public void printOptions() {
        System.out.println();
        System.out.println("[1] List species");
        System.out.println("[2] Add species");
        System.out.println("[3] View species detail");
        System.out.println("[4] Edit species");
        System.out.println("[5] Delete species");
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
