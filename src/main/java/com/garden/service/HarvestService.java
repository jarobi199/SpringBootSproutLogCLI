package com.garden.service;

import com.garden.authentication.SessionContext;
import com.garden.model.Bed;
import com.garden.model.Harvest;
import com.garden.model.Plant;
import com.garden.repository.BedRepository;
import com.garden.repository.HarvestRepository;
import com.garden.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HarvestService {
    @Autowired
    private HarvestRepository harvestRepository;
    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private PlantRepository plantRepository;


    public void logHarvest(Plant plant, LocalDate harvestDate, int quantity, String notes) {
        Bed bed = bedRepository.findById(plant.getBedId()).orElse(null);
        Harvest harvest = new Harvest(SessionContext.getUser().getId(), plant.getId(), plant.getName(), (bed != null) ? bed.getId() : "", plant.getSpeciesName(), harvestDate, quantity, plant.getYieldUnit(), notes);
        harvestRepository.save(harvest);
    }

    public List<Harvest> findAllHarvests() {
        return harvestRepository.findByUserId(SessionContext.getUser().getId());
    }

    public void deleteHarvest(Harvest harvest) {
        harvestRepository.delete(harvest);
    }

    public void historyByPlant() {
        Map<String, List<Harvest>> harvestMap =harvestRepository.findByUserId(SessionContext.getUser().getId()).stream()
                .sorted(Comparator.comparing(Harvest::getDate).reversed())
                .collect(Collectors.groupingBy(Harvest::getPlantId));

        for (Map.Entry<String, List<Harvest>> entry : harvestMap.entrySet()) {
            Plant plant = plantRepository.findById(entry.getKey()).orElse(null);
        }
    }
}
