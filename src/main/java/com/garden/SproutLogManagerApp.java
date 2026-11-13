package com.garden;

import com.garden.menu.MainMenu;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SproutLogManagerApp implements CommandLineRunner {

    @Autowired
    private MainMenu mainMenu;

    public static void main(String[] args) {
        SpringApplication.run(SproutLogManagerApp.class, args);
    }

    @Override
    public void run(String @NonNull ... args) {
        mainMenu.show();
    }

}