package com.garden.repository;

import com.garden.model.Species;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeciesRepository extends MongoRepository<Species, String> {
    List<Species> findByUserId(String userId);
}

