package com.garden.model;

import com.garden.enums.PlantStatus;
import com.garden.enums.Sun;
import com.garden.enums.YieldUnit;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

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
    private LocalDate plantingDate;
    private int squareFeet;
    private LocalDate lastWatered;
    private int daysToMaturity;
    private int wateringIntervalDays;
    private boolean frostTender;
    private Range expectedYield;
    private YieldUnit yieldUnit;
    private Sun sunNeed;

    public Plant() {
        //No argument constructor
    }

    public Plant(String userId, String bedId, String speciesId, String speciesName, String name, PlantStatus status, LocalDate plantingDate, int squareFeet,
                 LocalDate lastWatered, int daysToMaturity, int wateringIntervalDays, boolean frostTender, Range expectedYield, YieldUnit yieldUnit, Sun sunNeed) {
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
        this.yieldUnit = yieldUnit;
        this.sunNeed = sunNeed;
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

    public LocalDate getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(LocalDate plantingDate) {
        this.plantingDate = plantingDate;
    }

    public int getSquareFeet() {
        return squareFeet;
    }

    public void setSquareFeet(int squareFeet) {
        this.squareFeet = squareFeet;
    }

    public LocalDate getLastWatered() {
        return lastWatered;
    }

    public void setLastWatered(LocalDate lastWatered) {
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

    public YieldUnit getYieldUnit() {
        return yieldUnit;
    }

    public void setYieldUnit(YieldUnit yieldUnit) {
        this.yieldUnit = yieldUnit;
    }

    public Sun getSunNeed() {
        return sunNeed;
    }

    public void setSunNeed(Sun sunNeed) {
        this.sunNeed = sunNeed;
    }

    public LocalDate getNextWateringDate() {
        return lastWatered.plusDays(wateringIntervalDays);
    }

    public String getDetails() {
        return
                "Species Name: " + speciesName + "\n" +
                        "Name: " + name + "\n" +
                        "Status: " + status + "\n" +
                        "Planting Date: " + plantingDate + "\n" +
                        "Square Feet: " + squareFeet + "\n" +
                        "Last Watered: " + lastWatered + "\n" +
                        "Days to Maturity: " + daysToMaturity + "\n" +
                        "Watering Interval Days: " + wateringIntervalDays + "\n" +
                        "Frost Tender: " + frostTender + "\n" +
                        "Expected Yield: " + expectedYield + "\n" +
                        "Yield Unit: " + yieldUnit + "\n" +
                        "Sun Need: " + sunNeed + "\n" +
                        "Next Watering Date: " + getNextWateringDate();
    }

}
