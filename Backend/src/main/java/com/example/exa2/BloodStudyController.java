package com.example.exa2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/blood-studies")
public class BloodStudyController {
    private final BloodStudyRepository bloodStudyRepository;

    @Autowired
    public BloodStudyController(BloodStudyRepository bloodStudyRepository) {
        this.bloodStudyRepository = bloodStudyRepository;
    }

    @GetMapping
    public List<BloodStudy> getAllBloodStudies() {
        return bloodStudyRepository.findAll();
    }

    @GetMapping("/{nombreCompleto}")
    public ResponseEntity<List<BloodStudy>> getBloodStudiesByNombreCompleto(@PathVariable String nombreCompleto) {
        List<BloodStudy> bloodStudies = bloodStudyRepository.findByNombreCompleto(nombreCompleto);
        if (bloodStudies.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(bloodStudies);
        }
    }

    @PostMapping
    public ResponseEntity<?> createBloodStudy(@RequestBody BloodStudy bloodStudy) {
        if (bloodStudy.getNombreCompleto() == null || bloodStudy.getNombreCompleto().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre del paciente no puede ser nulo o vacío.");
        }

        if (!isPercentageValid(bloodStudy.getPorcentajeAzucar()) ||
                !isPercentageValid(bloodStudy.getPorcentajeGrasa()) ||
                !isPercentageValid(bloodStudy.getPorcentajeOxigeno())) {
            return ResponseEntity.badRequest().body("Los porcentajes deben estar en el rango válido [0-100].");
        }

        double sugarPercentage = bloodStudy.getPorcentajeAzucar();
        double fatPercentage = bloodStudy.getPorcentajeGrasa();
        double oxygenPercentage = bloodStudy.getPorcentajeOxigeno();
        String nivelRiesgo = calcularNivelRiesgo(sugarPercentage, fatPercentage, oxygenPercentage);
        bloodStudy.setNivelRiesgo(nivelRiesgo);
        bloodStudy.setFechaCreacion(new Date());

        BloodStudy createdBloodStudy = bloodStudyRepository.save(bloodStudy);
        return new ResponseEntity<>(createdBloodStudy, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBloodStudy(@PathVariable String id, @RequestBody BloodStudy bloodStudy) {
        if (bloodStudy.getNombreCompleto() == null || bloodStudy.getNombreCompleto().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre del paciente no puede ser nulo o vacío.");
        }

        if (!isPercentageValid(bloodStudy.getPorcentajeAzucar()) ||
                !isPercentageValid(bloodStudy.getPorcentajeGrasa()) ||
                !isPercentageValid(bloodStudy.getPorcentajeOxigeno())) {
            return ResponseEntity.badRequest().body("Los porcentajes deben estar en el rango válido [0-100].");
        }

        BloodStudy existingBloodStudy = bloodStudyRepository.findById(id).orElse(null);

        if (existingBloodStudy != null) {
            existingBloodStudy.setNombreCompleto(bloodStudy.getNombreCompleto());
            existingBloodStudy.setPorcentajeAzucar(bloodStudy.getPorcentajeAzucar());
            existingBloodStudy.setPorcentajeGrasa(bloodStudy.getPorcentajeGrasa());
            existingBloodStudy.setPorcentajeOxigeno(bloodStudy.getPorcentajeOxigeno());
            existingBloodStudy.setNivelRiesgo(calcularNivelRiesgo(bloodStudy.getPorcentajeAzucar(), bloodStudy.getPorcentajeGrasa(), bloodStudy.getPorcentajeOxigeno()));
            existingBloodStudy.setFechaActualizacion(new Date());

            BloodStudy updatedBloodStudy = bloodStudyRepository.save(existingBloodStudy);
            return new ResponseEntity<>(updatedBloodStudy, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBloodStudy(@PathVariable String id) {
        BloodStudy bloodStudy = bloodStudyRepository.findById(id).orElse(null);

        if (bloodStudy != null) {
            bloodStudyRepository.delete(bloodStudy);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private boolean isPercentageValid(double percentage) {
        return percentage >= 0 && percentage <= 100;
    }

    private String calcularNivelRiesgo(double sugarPercentage, double fatPercentage, double oxygenPercentage) {
        if (sugarPercentage > 70 && fatPercentage > 88.5 && oxygenPercentage < 60) {
            return "ALTO";
        } else if (sugarPercentage >= 50 && sugarPercentage <= 70 && fatPercentage >= 62.2 && fatPercentage <= 88.5 && oxygenPercentage >= 60 && oxygenPercentage <= 70) {
            return "MEDIO";
        } else if (sugarPercentage < 50 && fatPercentage < 62.2 && oxygenPercentage > 70) {
            return "BAJO";
        } else {
            return "DESCONOCIDO";
        }
    }
}