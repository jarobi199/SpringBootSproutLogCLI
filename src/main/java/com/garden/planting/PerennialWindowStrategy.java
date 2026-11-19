package com.garden.planting;

import com.garden.interfaces.PlantingWindowStrategy;
import com.garden.model.PerennialSpecies;
import com.garden.model.PlantingWindow;
import com.garden.model.User;

import java.time.LocalDate;

public class PerennialWindowStrategy implements PlantingWindowStrategy<PerennialSpecies> {

    @Override
    public PlantingWindow window(PerennialSpecies species, User user, int year) {
        if (!species.isHardyIn(user.getHardinessZone())) {
            return new PlantingWindow(LocalDate.MAX, LocalDate.MIN);
        }

        LocalDate lastFrost = user.getLastSpringFrost().atYear(year);

        LocalDate opens  = lastFrost.plusDays(species.getWindowOffsets().min());
        LocalDate closes = lastFrost.plusDays(species.getWindowOffsets().max());

        return new PlantingWindow(opens, closes);
    }
}