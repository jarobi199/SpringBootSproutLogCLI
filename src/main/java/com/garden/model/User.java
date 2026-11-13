package com.garden.model;

import com.garden.enums.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String username;
    private String password;
    private Role role;
    private int hardinessZone;
    private SeasonDay lastSpringFrost;
    private SeasonDay firstFallFrost;
    private int wateringLeadDays;
    private int frostWarningDays;
    private int plantingLeadDays;

    public User() {
        //No argument constructor
    }

    public User(String name, String username, String password, Role role, int hardinessZone, SeasonDay lastSpringFrost, SeasonDay firstFallFrost,
                int wateringLeadDays, int frostWarningDays, int plantingLeadDays) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.hardinessZone = hardinessZone;
        this.lastSpringFrost = lastSpringFrost;
        this.firstFallFrost = firstFallFrost;
        this.wateringLeadDays = wateringLeadDays;
        this.frostWarningDays = frostWarningDays;
        this.plantingLeadDays = plantingLeadDays;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getHardinessZone() {
        return hardinessZone;
    }

    public void setHardinessZone(int hardinessZone) {
        this.hardinessZone = hardinessZone;
    }

    public SeasonDay getLastSpringFrost() {
        return lastSpringFrost;
    }

    public void setLastSpringFrost(SeasonDay lastSpringFrost) {
        this.lastSpringFrost = lastSpringFrost;
    }

    public SeasonDay getFirstFallFrost() {
        return firstFallFrost;
    }

    public void setFirstFallFrost(SeasonDay firstFallFrost) {
        this.firstFallFrost = firstFallFrost;
    }

    public int getWateringLeadDays() {
        return wateringLeadDays;
    }

    public void setWateringLeadDays(int wateringLeadDays) {
        this.wateringLeadDays = wateringLeadDays;
    }

    public int getFrostWarningDays() {
        return frostWarningDays;
    }

    public void setFrostWarningDays(int frostWarningDays) {
        this.frostWarningDays = frostWarningDays;
    }

    public int getPlantingLeadDays() {
        return plantingLeadDays;
    }

    public void setPlantingLeadDays(int plantingLeadDays) {
        this.plantingLeadDays = plantingLeadDays;
    }
}

