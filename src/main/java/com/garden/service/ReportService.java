package com.garden.service;

import com.garden.repository.BedRepository;
import com.garden.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private PlantRepository plantRepository;

}
