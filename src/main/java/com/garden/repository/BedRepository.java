package com.garden.repository;

import com.garden.model.Bed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends MongoRepository<Bed, String> {
    List<Bed> findByUserId(String userId);
    void deleteByUserId(String userId);
}

