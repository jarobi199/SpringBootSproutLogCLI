package com.garden.model;

import com.garden.enums.SpeciesCategory;
import com.garden.enums.Sun;
import com.garden.enums.YieldUnit;
import com.garden.interfaces.PlantingWindowStrategy;
import com.garden.planting.WarmSeasonWindowStrategy;
import org.springframework.data.annotation.Transient;

public class WarmSeasonSpecies extends Species {

    @Transient
    private PlantingWindowStrategy<WarmSeasonSpecies> plantingWindowStrategy;

    public WarmSeasonSpecies() {
        this.plantingWindowStrategy = new WarmSeasonWindowStrategy();
    }

    public WarmSeasonSpecies(String userId, String name, Sun sunNeed, int daysToMaturity, int wateringIntervalDays, boolean frostTender, Range windowOffsets, Range expectedYield, YieldUnit yieldUnit) {
        super(userId, name, sunNeed, daysToMaturity, wateringIntervalDays, frostTender, windowOffsets, expectedYield, yieldUnit);
    }

    @Override
    public SpeciesCategory getCategory() {
        return SpeciesCategory.WARM_SEASON;
    }

    @Override
    public PlantingWindow plantingWindow(User user, int year) {
        return plantingWindowStrategy.window(this, user, year);
    }

}
