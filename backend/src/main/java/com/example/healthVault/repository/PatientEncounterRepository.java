package com.example.healthVault.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.healthVault.model.PatientEncounter;

public interface PatientEncounterRepository extends JpaRepository<PatientEncounter, Long> {
    List<PatientEncounter> findByPatientId(Long patientId);
}
