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
    private GoodbyeMenu goodbyeMenu;

    public void show() {
        int choice = 0;
        IMenu menu;

        System.out.println();
        displayTitle();
        System.out.println();

        authenticateMenu.show();
        System.out.println();

        if(SessionContext.isAuthenticated()) {
            do {
                printOptions();
                choice = InputHandler.getIntegerInput();
                menu = switch (choice) {
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
