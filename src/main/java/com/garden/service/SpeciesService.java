package com.garden.service;

import com.garden.authentication.SessionContext;
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

}
