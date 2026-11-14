package com.garden.model;

import com.garden.enums.PlantStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "plants")
public class Plant {
    @Id
    private String id;
    private String userId;
    private String bedId;
    private String speciesId;
    private String speciesName;
    private String name;
    private PlantStatus status;
    private LocalDateTime plantingDate;
    private int squareFeet;
    private LocalDateTime lastWatered;
    private int daysToMaturity;
    private int wateringIntervalDays;
    private boolean frostTender;
    private Range expectedYield;

    public Plant() {
        //No argument constructor
    }

    public Plant(String userId, String bedId, String speciesId, String speciesName, String name, PlantStatus status, LocalDateTime plantingDate, int squareFeet,
                 LocalDateTime lastWatered, int daysToMaturity, int wateringIntervalDays, boolean frostTender, Range expectedYield) {
        this.userId = userId;
        this.bedId = bedId;
        this.speciesId = speciesId;
        this.speciesName = speciesName;
        this.name = name;
        this.status = status;
        this.plantingDate = plantingDate;
        this.squareFeet = squareFeet;
        this.lastWatered = lastWatered;
        this.daysToMaturity = daysToMaturity;
        this.wateringIntervalDays = wateringIntervalDays;
        this.frostTender = frostTender;
        this.expectedYield = expectedYield;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBedId() {
        return bedId;
    }

    public void setBedId(String bedId) {
        this.bedId = bedId;
    }

    public String getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(String speciesId) {
        this.speciesId = speciesId;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlantStatus getStatus() {
        return status;
    }

    public void setStatus(PlantStatus status) {
        this.status = status;
    }

    public LocalDateTime getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(LocalDateTime plantingDate) {
        this.plantingDate = plantingDate;
    }

    public int getSquareFeet() {
        return squareFeet;
    }

    public void setSquareFeet(int squareFeet) {
        this.squareFeet = squareFeet;
    }

    public LocalDateTime getLastWatered() {
        return lastWatered;
    }

    public void setLastWatered(LocalDateTime lastWatered) {
        this.lastWatered = lastWatered;
    }

    public int getDaysToMaturity() {
        return daysToMaturity;
    }

    public void setDaysToMaturity(int daysToMaturity) {
        this.daysToMaturity = daysToMaturity;
    }

    public int getWateringIntervalDays() {
        return wateringIntervalDays;
    }

    public void setWateringIntervalDays(int wateringIntervalDays) {
        this.wateringIntervalDays = wateringIntervalDays;
    }

    public boolean isFrostTender() {
        return frostTender;
    }

    public void setFrostTender(boolean frostTender) {
        this.frostTender = frostTender;
    }

    public Range getExpectedYield() {
        return expectedYield;
    }

    public void setExpectedYield(Range expectedYield) {
        this.expectedYield = expectedYield;
    }
}
