package com.garden.model;

import com.garden.enums.SpeciesCategory;
import com.garden.enums.Sun;
import com.garden.enums.YieldUnit;

public abstract class Species {
    private String id;
    private String userId;
    private String name;
    private Sun sunNeed;
    private int daysToMaturity;
    private int wateringIntervalDays;
    private boolean frostTender;
    private Range windowOffsets;
    private Range expectedYield;
    private YieldUnit yieldUnit;

    public Species() {
        //No argument constructor
    }

    public Species(String userId, String name, Sun sunNeed, int daysToMaturity, int wateringIntervalDays, boolean frostTender, Range windowOffsets, Range expectedYield, YieldUnit yieldUnit) {
        this.userId = userId;
        this.name = name;
        this.sunNeed = sunNeed;
        this.daysToMaturity = daysToMaturity;
        this.wateringIntervalDays = wateringIntervalDays;
        this.frostTender = frostTender;
        this.windowOffsets = windowOffsets;
        this.expectedYield = expectedYield;
        this.yieldUnit = yieldUnit;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sun getSunNeed() {
        return sunNeed;
    }

    public void setSunNeed(Sun sunNeed) {
        this.sunNeed = sunNeed;
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

    public Range getWindowOffsets() {
        return windowOffsets;
    }

    public void setWindowOffsets(Range windowOffsets) {
        this.windowOffsets = windowOffsets;
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

    public abstract SpeciesCategory getCategory();

    public abstract PlantingWindow plantingWindow(User user, int year);

    public boolean isHardyIn(int zone) {
        return true;
    }

}