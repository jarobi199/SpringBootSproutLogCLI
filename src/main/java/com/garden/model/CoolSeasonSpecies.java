package com.garden.model;

import com.garden.enums.SpeciesCategory;
import com.garden.enums.Sun;
import com.garden.enums.YieldUnit;

public class CoolSeasonSpecies extends Species {

    public CoolSeasonSpecies() {
        //No argument constructor
    }

    public CoolSeasonSpecies(String userId, String name, Sun sunNeed, int daysToMaturity, int wateringIntervalDays, boolean frostTender, Range windowOffsets, Range expectedYield, YieldUnit yieldUnit) {
        super(userId, name, sunNeed, daysToMaturity, wateringIntervalDays, frostTender, windowOffsets, expectedYield, yieldUnit);
    }

    @Override
    public SpeciesCategory getCategory() {
        return SpeciesCategory.COOL_SEASON;
    }

    @Override
    public PlantingWindow plantingWindow(User user, int year) {
        return null;
    }

}
