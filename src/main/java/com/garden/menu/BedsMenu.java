package com.garden.menu;

import com.garden.enums.BedType;
import com.garden.enums.Sun;
import com.garden.interfaces.IMenu;
import com.garden.model.Bed;
import com.garden.service.BedService;
import com.garden.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BedsMenu implements IMenu {
    @Autowired
    private BedService bedService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> listBeds();
                case 2 -> addBed();
                case 3 -> viewBedDetail();
                case 4 -> editBed();
                case 5 -> deleteBed();
            }
        }
        while (choice != 0);
    }

    public void editBed() {
        Bed bed = listBedsAndSelect();
        if (bed != null) {
            System.out.println("Enter the new name of the bed:");
            String bedName = InputHandler.getStringInput();
            System.out.println("Enter the new sun exposure (SHADE, PART_SUN, FULL_SUN):");
            Sun sunExposure = Sun.valueOf(InputHandler.getStringInput().toUpperCase());
            System.out.println("Enter the new area in square feet:");
            int squareFeet = InputHandler.getIntegerInput();

            bedService.editBed(bed, bedName, sunExposure, squareFeet);
        }
    }

    public void viewBedDetail() {
        Bed bed = listBedsAndSelect();
        if (bed != null) {
            bedService.viewBedDetail(bed);
        }
    }

    public void deleteBed() {
        Bed bed = listBedsAndSelect();
        if (bed != null) {
            System.out.println("Are you sure that you want to delete this bed? (Y/N):");
            boolean toDelete = InputHandler.getBooleanInput();
            if (toDelete) {
                bedService.deleteBed(bed);
            }
        }
    }

    public void listBeds() {
        bedService.listBeds();
    }

    public void addBed() {
        System.out.println("Enter the name of the bed:");
        String bedName = InputHandler.getStringInput();
        System.out.println("Enter the bed type (RAISED, IN_GROUND, CONTAINER):");
        BedType bedType = BedType.valueOf(InputHandler.getStringInput().toUpperCase());
        System.out.println("Enter the sun exposure (SHADE, PART_SUN, FULL_SUN):");
        Sun sunExposure = Sun.valueOf(InputHandler.getStringInput().toUpperCase());
        System.out.println("Enter the area in square feet:");
        int squareFeet = InputHandler.getIntegerInput();

        bedService.addBed(bedName, bedType, sunExposure, squareFeet);
        System.out.println("The new bed was successfully added!");
    }

    public void printOptions() {
        System.out.println();
        System.out.println("[1] List beds");
        System.out.println("[2] Add bed");
        System.out.println("[3] View bed detail");
        System.out.println("[4] Edit bed");
        System.out.println("[5] Delete bed");
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }

    public Bed listBedsAndSelect() {
        int number = 1;
        Bed bed = null;
        int choice;

        List<Bed> beds = bedService.getAllBeds();

        if(!beds.isEmpty()) {
            for (Bed b : beds) {
                System.out.println("[" + number + "] " +  b.getName() + " (" + b.getBedType() + ")");
                number++;
            }
            System.out.println("Select a bed:");
            choice = InputHandler.getIntegerInput();
            bed = beds.get(choice - 1);
        }
        else
        {
            System.out.println("There are no beds available.");
        }

        return bed;
    }


}
