package com.garden.seed;

import com.garden.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpeciesSeeder {
   /* @Autowired
    private SpeciesRepository speciesRepository;

    public void seed(User user) {
        speciesRepository.saveAll(starterCatalog(user.getId()));
    }

    private List<Species> starterCatalog(String userId) {
        return List.of(
                new WarmSeasonSpecies(userId, "Tomato", Sun.FULL_SUN, 75, 2, true,
                        new Range(14, 56), new Range(128, 320), YieldUnit.OUNCES),
                new CoolSeasonSpecies(userId, "Lettuce", Sun.PART_SUN, 45, 2, false,
                        new Range(-42, 14), new Range(8, 16), YieldUnit.OUNCES),
                new PerennialSpecies(userId, "Strawberry", Sun.FULL_SUN, 90, 2, false,
                        new Range(-28, 28), new Range(12, 32), YieldUnit.OUNCES, 5)
                // ... the other starter species from the table above ...
        );
    }*/
}
