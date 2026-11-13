package com.garden.menu;

import com.garden.authentication.SessionContext;
import com.garden.enums.Role;
import com.garden.interfaces.IMenu;
import com.garden.model.SeasonDay;
import com.garden.service.UserService;
import com.garden.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SettingsMenu implements IMenu {
    @Autowired
    private UserService userService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> changePassword();
                //case 2 -> setFavoriteStockThreshold();
                case 6 -> deleteUser();
                case 8 -> createUser();
            }
        }
        while (choice != 0);
    }

    public void changePassword() {
        System.out .println("Enter the new password:");
        String newPassword = InputHandler.getStringInput();
        userService.changePassword(newPassword);
    }

    public void createUser() {
        System.out.println("Enter the full name:");
        String fullName = InputHandler.getStringInput();
        System.out.println("Enter the username:");
        String username = InputHandler.getStringInput();
        System.out.println("Enter the password:");
        String password = InputHandler.getStringInput();
        System.out.println("Enter the role (ADMINISTRATOR, USER):");
        Role role = Role.valueOf(InputHandler.getStringInput().toUpperCase());
        System.out.println("Enter the hardiness zone value:");
        int hardinessZone = InputHandler.getIntegerInput();
        System.out.println("Enter the last spring frost as a month and day separated by a comma (example: 5,10):");
        List<String> lastSpringInputs = List.of(InputHandler.getStringInput().split(","));
        SeasonDay lastSpringFrost = new SeasonDay(Integer.parseInt(lastSpringInputs.getFirst()), Integer.parseInt(lastSpringInputs.get(1)));
        System.out.println("Enter the first fall frost as a month and day separated by a comma (example: 5,10):");
        List<String> firstFallInputs = List.of(InputHandler.getStringInput().split(","));
        SeasonDay firstFallFrost = new SeasonDay(Integer.parseInt(firstFallInputs.getFirst()), Integer.parseInt(firstFallInputs.get(1))) ;

        userService.register(fullName, username, password, role, hardinessZone, lastSpringFrost, firstFallFrost, 1, 5, 7);
        System.out.println("Your new user has been created!");
    }

    public void deleteUser() {
        if(verifyAdmin()) {
            System.out.println("Enter the username of the user that you want to delete:");
            String username = InputHandler.getStringInput();
            System.out.println("Are you sure you want to delete this user? (Y/N):");
            String answer = InputHandler.getStringInput();
            if("Y".equalsIgnoreCase(answer)) {
                userService.deleteUser(username);
            }
        }
    }

    public void printOptions() {
        System.out.println();
        System.out.println("[1] Change password");
        System.out.println("[2] Set hardiness zone");
        System.out.println("[3] Set frost dates");
        System.out.println("[4] Set watering lead days");
        System.out.println("[5] Set frost warning days");
        System.out.println("[6] Set planting lead days");
        System.out.println("[7] Restore starter species");
        if(verifyAdmin()) {
            System.out.println("[8] Add user");
            System.out.println("[9] Delete user");
        }
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }

    private boolean verifyAdmin() {
        return Role.ADMINISTRATOR.equals(SessionContext.getUser().getRole());
    }
}
