package com.example.healthVault.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.healthVault.model.PatientAllergy;

public interface PatientAllergyRepository extends JpaRepository<PatientAllergy, Long> {
    List<PatientAllergy> findByPatientId(Long patientId);
}
