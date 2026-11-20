package com.garden.alert;

import com.garden.model.Plant;
import com.garden.model.Species;
import com.garden.model.User;

import java.util.List;
import java.util.Map;

public record AlertContext(User user, List<Plant> plants, Map<String, Species> species) {
    public Species speciesFor(Plant plant) {
        return species.get(plant.getSpeciesId());
    }
}