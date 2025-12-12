package com.example.healthVault.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.healthVault.dto.PatientDTO;
import com.example.healthVault.model.Patient;
import com.example.healthVault.repository.PatientConditionRepository;
import com.example.healthVault.repository.PatientRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ApiPatientController {

    private final PatientRepository patientRepo;
    private final PatientConditionRepository conditionRepo;

    public ApiPatientController(
            PatientRepository patientRepo,
            PatientConditionRepository conditionRepo) {
        this.patientRepo = patientRepo;
        this.conditionRepo = conditionRepo;
    }

    @GetMapping("/patients")
    public List<PatientDTO> getPatients() {
        return patientRepo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/patients/{id}")
    public PatientDTO getPatientById(@PathVariable Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow();
        return convertToDTO(patient);
    }

    private PatientDTO convertToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO(patient);
        var conditions = conditionRepo.findByPatientId(patient.getId());

        System.out.println("Patient ID: " + patient.getId() + " -> Conditions: " + conditions.size());

        dto.setConditions(conditions);
        return dto;
    }
}
