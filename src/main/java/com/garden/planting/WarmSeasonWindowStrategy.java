package com.garden.planting;

import com.garden.interfaces.PlantingWindowStrategy;
import com.garden.model.PlantingWindow;
import com.garden.model.User;
import com.garden.model.WarmSeasonSpecies;

import java.time.LocalDate;

public class WarmSeasonWindowStrategy implements PlantingWindowStrategy<WarmSeasonSpecies> {

    @Override
    public PlantingWindow window(WarmSeasonSpecies species, User user, int year) {
        LocalDate lastFrost = user.getLastSpringFrost().atYear(year);
        LocalDate firstFrost = user.getFirstFallFrost().atYear(year);

        LocalDate opens = lastFrost.plusDays(species.getWindowOffsets().min());

        LocalDate byOffset = lastFrost.plusDays(species.getWindowOffsets().max());
        LocalDate byMaturity = firstFrost.minusDays(species.getDaysToMaturity());
        LocalDate closes = byOffset.isBefore(byMaturity) ? byOffset : byMaturity;

        // If closes < opens the season is too short; PlantingWindow.isEmpty() reports it.
        return new PlantingWindow(opens, closes);
    }

}
