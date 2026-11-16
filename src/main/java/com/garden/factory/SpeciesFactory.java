package com.garden.factory;

import com.garden.authentication.SessionContext;
import com.garden.enums.SpeciesCategory;
import com.garden.enums.Sun;
import com.garden.enums.YieldUnit;
import com.garden.model.*;

public class SpeciesFactory {

    public static Species createSpecies(SpeciesCategory speciesCategory, String name, Sun sun, int maturityDays, int wateringInterval,
                                        boolean frostTender, Range windowOffsets, Range expectedYield, YieldUnit yieldUnit, int minHardinessZone) {
        return switch (speciesCategory) {
            case COOL_SEASON -> new CoolSeasonSpecies(SessionContext.getUser().getId(), name, sun, maturityDays, wateringInterval, frostTender, windowOffsets, expectedYield, yieldUnit);
            case WARM_SEASON -> new WarmSeasonSpecies(SessionContext.getUser().getId(), name, sun, maturityDays, wateringInterval, frostTender, windowOffsets, expectedYield, yieldUnit);
            case PERENNIAL -> new PerennialSpecies(SessionContext.getUser().getId(), name, sun, maturityDays, wateringInterval, frostTender, windowOffsets, expectedYield, yieldUnit, minHardinessZone);
        };
    }

}
