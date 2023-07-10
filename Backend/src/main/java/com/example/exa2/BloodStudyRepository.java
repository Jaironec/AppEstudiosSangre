package com.example.exa2;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodStudyRepository extends MongoRepository<BloodStudy, String> {
    List<BloodStudy> findByNombreCompleto(String nombreCompleto);
}
