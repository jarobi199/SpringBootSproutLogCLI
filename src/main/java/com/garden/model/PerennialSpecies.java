package com.garden.model;

import com.garden.enums.SpeciesCategory;
import com.garden.enums.Sun;
import com.garden.enums.YieldUnit;

public class PerennialSpecies extends Species {
    private int minZone;

    public PerennialSpecies() {
        //No argument constructor
    }

    public PerennialSpecies(String userId, String name, Sun sunNeed, int daysToMaturity, int wateringIntervalDays, boolean frostTender, Range windowOffsets, Range expectedYield, YieldUnit yieldUnit, int minZone) {
        super(userId, name, sunNeed, daysToMaturity, wateringIntervalDays, frostTender, windowOffsets, expectedYield, yieldUnit);
        this.minZone = minZone;
    }

    public int getMinZone() {
        return minZone;
    }

    public void setMinZone(int minZone) {
        this.minZone = minZone;
    }

    @Override
    public SpeciesCategory getCategory() {
        return null;
    }

    @Override
    public PlantingWindow plantingWindow(User user, int year) {
        return null;
    }
}
