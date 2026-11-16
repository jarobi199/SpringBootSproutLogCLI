package com.garden.menu;

import com.garden.authentication.SessionContext;
import com.garden.interfaces.IMenu;
import com.garden.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainMenu implements IMenu {
    @Autowired
    private AuthenticateMenu authenticateMenu;
    @Autowired
    private SettingsMenu settingsMenu;
    @Autowired
    private BedsMenu bedsMenu;
    @Autowired
    private PlantsMenu plantsMenu;
    @Autowired
    private SpeciesMenu speciesMenu;
    @Autowired
    private GoodbyeMenu goodbyeMenu;

    public void show() {
        int choice = 0;
        IMenu menu;

        System.out.println();
        displayTitle();
        System.out.println();

        authenticateMenu.automaticLogin();
        System.out.println();

        if(SessionContext.isAuthenticated()) {
            do {
                printOptions();
                choice = InputHandler.getIntegerInput();
                menu = switch (choice) {
                    case 1 -> bedsMenu;
                    case 2 -> plantsMenu;
                    case 3 -> speciesMenu;
                    case 7 -> settingsMenu;
                    case 0 -> goodbyeMenu;
                    default -> throw new IllegalStateException("Unexpected value: " + choice);
                };
                menu.show();
            }
            while (choice != 0);

            InputHandler.closeInput();
        }
    }

    public void printOptions() {
        System.out.println("[1] Beds");
        System.out.println("[2] Plants");
        System.out.println("[3] Species");
        System.out.println("[4] Care");
        System.out.println("[5] Harvests");
        System.out.println("[6] Reports");
        System.out.println("[7] Alerts");
        System.out.println("[8] Settings");
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }

    public void displayTitle() {
        System.out.println("=======================================================================");
        System.out.println("   Welcome to the Sprout Log Garden & Plant Care Planner Application!");
        System.out.println("=======================================================================");
    }

}
