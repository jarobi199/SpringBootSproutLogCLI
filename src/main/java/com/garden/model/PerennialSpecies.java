package com.garden.model;

import com.garden.enums.SpeciesCategory;
import com.garden.enums.Sun;
import com.garden.enums.YieldUnit;
import com.garden.interfaces.PlantingWindowStrategy;
import com.garden.planting.PerennialWindowStrategy;
import org.springframework.data.annotation.Transient;

public class PerennialSpecies extends Species {
    private int minZone;
    @Transient
    private PlantingWindowStrategy<PerennialSpecies> plantingWindowStrategy;

    public PerennialSpecies() {
        this.plantingWindowStrategy = new PerennialWindowStrategy();
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
    public boolean isHardyIn(int zone) {
        return zone >= minZone;
    }

    @Override
    public SpeciesCategory getCategory() {
        return SpeciesCategory.PERENNIAL;
    }

    @Override
    public PlantingWindow plantingWindow(User user, int year) {
        return plantingWindowStrategy.window(this, user, year);
    }
}
