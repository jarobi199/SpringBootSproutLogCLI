package com.garden.service;

import com.garden.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlantService {
    @Autowired
    private PlantRepository plantRepository;


}
