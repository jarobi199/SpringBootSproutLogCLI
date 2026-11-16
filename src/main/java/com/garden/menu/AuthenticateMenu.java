package com.garden.menu;

import com.garden.interfaces.IMenu;
import com.garden.service.AuthenticationService;
import com.garden.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateMenu implements IMenu {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private SettingsMenu settingsMenu;

    @Override
    public void show() {
        int choice;

        printOptions();
        choice = InputHandler.getIntegerInput();
        switch (choice) {
            case 1 -> authenticateUser();
            case 2 -> settingsMenu.createUser();
        }
    }

    public void authenticateUser() {
        boolean authenticated = false;

        while (!authenticated) {
            System.out.println("Please enter your username:");
            String username = InputHandler.getStringInput();
            System.out.println("Please enter your password:");
            String password = InputHandler.getStringInput();
            authenticated = authenticationService.authenticate(username, password);
            if (authenticated) {
                System.out.println("You have successfully logged in with the username: " + username + "!");
            } else {
                System.out.println("Invalid username or password!\n");
            }
        }
    }

    @Override
    public void printOptions() {
        System.out.println("[1] Login");
        System.out.println("[2] Create new user");
        System.out.println("Please make a selection:");
    }

    public void automaticLogin() {
        authenticationService.authenticate("dstucky", "password");
    }

}
