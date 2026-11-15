package com.garden.repository;

import com.garden.enums.PlantStatus;
import com.garden.model.Plant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends MongoRepository<Plant, String> {
    List<Plant> findByUserId(String userId);
    List<Plant> findByUserIdAndStatus(String userId, PlantStatus status);
    List<Plant> findByBedId(String bedId);
    List<Plant> findBySpeciesId(String speciesId);
}
