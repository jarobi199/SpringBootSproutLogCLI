package com.garden.seed;

import com.garden.enums.Sun;
import com.garden.enums.YieldUnit;
import com.garden.model.*;
import com.garden.repository.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpeciesSeeder {
    @Autowired
    private SpeciesRepository speciesRepository;

    public void seed(User user) {
        speciesRepository.saveAll(starterCatalog(user.getId()));
    }

    private List<Species> starterCatalog(String userId) {
        return List.of(
                // Warm-season
                new WarmSeasonSpecies(userId, "Tomato", Sun.FULL_SUN, 75, 2, true,
                        new Range(14, 56), new Range(128, 320), YieldUnit.OUNCES),
                new WarmSeasonSpecies(userId, "Pepper", Sun.FULL_SUN, 70, 3, true,
                        new Range(14, 49), new Range(48, 128), YieldUnit.OUNCES),
                new WarmSeasonSpecies(userId, "Cucumber", Sun.FULL_SUN, 55, 2, true,
                        new Range(7, 42), new Range(96, 256), YieldUnit.OUNCES),
                new WarmSeasonSpecies(userId, "Zucchini", Sun.FULL_SUN, 50, 2, true,
                        new Range(7, 42), new Range(128, 320), YieldUnit.OUNCES),
                new WarmSeasonSpecies(userId, "Green Bean", Sun.FULL_SUN, 55, 3, true,
                        new Range(7, 49), new Range(16, 48), YieldUnit.OUNCES),

                // Cool-season
                new CoolSeasonSpecies(userId, "Lettuce", Sun.PART_SUN, 45, 2, false,
                        new Range(-42, 14), new Range(8, 16), YieldUnit.OUNCES),
                new CoolSeasonSpecies(userId, "Peas", Sun.FULL_SUN, 65, 2, false,
                        new Range(-56, -14), new Range(8, 24), YieldUnit.OUNCES),
                new CoolSeasonSpecies(userId, "Spinach", Sun.PART_SUN, 40, 3, false,
                        new Range(-42, 0), new Range(6, 12), YieldUnit.OUNCES),
                new CoolSeasonSpecies(userId, "Carrot", Sun.FULL_SUN, 70, 3, false,
                        new Range(-28, 28), new Range(8, 20), YieldUnit.OUNCES),
                new CoolSeasonSpecies(userId, "Broccoli", Sun.FULL_SUN, 80, 3, false,
                        new Range(-35, 7), new Range(12, 32), YieldUnit.OUNCES),

                // Perennial
                new PerennialSpecies(userId, "Strawberry", Sun.FULL_SUN, 90, 2, false,
                        new Range(-28, 28), new Range(12, 32), YieldUnit.OUNCES, 5),
                new PerennialSpecies(userId, "Rosemary", Sun.FULL_SUN, 120, 7, false,
                        new Range(0, 60), new Range(2, 8), YieldUnit.OUNCES, 7),
                new PerennialSpecies(userId, "Asparagus", Sun.FULL_SUN, 730, 4, false,
                        new Range(-28, 14), new Range(16, 64), YieldUnit.OUNCES, 3)
        );
    }

}
