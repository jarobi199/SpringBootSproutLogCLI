package com.garden.menu;

import com.garden.interfaces.IMenu;
import com.garden.service.ReportService;
import com.garden.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportsMenu implements IMenu {
    @Autowired
    private ReportService reportService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> plantNow();
                case 2 -> plantingCalendar();
                case 3 -> yieldSummary();
                case 4 -> bedUtilization();
            }
        }
        while (choice != 0);
    }

    public void plantingCalendar() {
        System.out.println("Please enter a year:");
        int year = InputHandler.getIntegerInput();
        if(year > 1900) {
            reportService.plantingCalendar(year);
        }
    }

    public void yieldSummary() {
        System.out.println("Please enter a year:");
        int year = InputHandler.getIntegerInput();
        if(year > 1900) {
            reportService.yieldSummary(year);
        }
    }

    public void bedUtilization() {
        reportService.bedUtilization();
    }

    public void plantNow() {
        reportService.plantNow();
    }

    public void printOptions() {
        System.out.println();
        System.out.println("[1] What can I plant now");
        System.out.println("[2] Planting calendar");
        System.out.println("[3] Yield summary");
        System.out.println("[4] Bed utilization");
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }
}
