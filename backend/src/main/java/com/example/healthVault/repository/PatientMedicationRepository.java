package com.example.healthVault.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.healthVault.model.PatientMedication;

public interface PatientMedicationRepository extends JpaRepository<PatientMedication, Long> {
    List<PatientMedication> findByPatientId(Long patientId);
}
