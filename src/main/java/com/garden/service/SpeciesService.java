package com.garden.service;

import com.garden.authentication.SessionContext;
import com.garden.enums.SpeciesCategory;
import com.garden.enums.Sun;
import com.garden.enums.YieldUnit;
import com.garden.factory.SpeciesFactory;
import com.garden.model.Range;
import com.garden.model.Species;
import com.garden.repository.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpeciesService {
    @Autowired
    private SpeciesRepository speciesRepository;

    public List<Species> getAllSpecies() {
        return speciesRepository.findByUserId(SessionContext.getUser().getId());
    }

    public void addSpecies(SpeciesCategory speciesCategory, String name, Sun sun, int maturityDays, int wateringInterval, boolean frostTender, Range windowOffsets,
                           Range expectedYield, YieldUnit yieldUnit, int minHardinessZone) {
        Species species = SpeciesFactory.createSpecies(speciesCategory, name, sun, maturityDays, wateringInterval, frostTender, windowOffsets, expectedYield, yieldUnit, minHardinessZone);
        speciesRepository.save(species);
    }

}
