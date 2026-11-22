package com.garden.service;

import com.garden.authentication.SessionContext;
import com.garden.model.Species;
import com.garden.model.User;
import com.garden.repository.BedRepository;
import com.garden.repository.PlantRepository;
import com.garden.repository.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private SpeciesRepository speciesRepository  ;
    @Autowired
    private SpeciesService speciesService;

    public void plantNow() {
        User user = SessionContext.getUser();
        LocalDate today = LocalDate.now();
        List<Species> speciesList = speciesRepository.findByUserId(user.getId()).stream().filter(species -> species.plantingWindow(user, today.getYear()).isOpenOn(today)).toList();
    }
}
