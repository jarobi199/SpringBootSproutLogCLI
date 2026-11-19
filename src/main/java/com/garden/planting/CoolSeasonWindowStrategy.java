package com.garden.planting;

import com.garden.interfaces.PlantingWindowStrategy;
import com.garden.model.CoolSeasonSpecies;
import com.garden.model.PlantingWindow;
import com.garden.model.User;

import java.time.LocalDate;

public class CoolSeasonWindowStrategy implements PlantingWindowStrategy<CoolSeasonSpecies> {

    @Override
    public PlantingWindow window(CoolSeasonSpecies species, User user, int year) {
        LocalDate lastFrost = user.getLastSpringFrost().atYear(year);

        LocalDate opens  = lastFrost.plusDays(species.getWindowOffsets().min());
        LocalDate closes = lastFrost.plusDays(species.getWindowOffsets().max());
        // No fall-frost cap: cool-season crops tolerate frost.

        return new PlantingWindow(opens, closes);
    }
}