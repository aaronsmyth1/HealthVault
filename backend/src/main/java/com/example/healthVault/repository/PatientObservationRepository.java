package com.example.healthVault.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.healthVault.model.PatientObservation;

public interface PatientObservationRepository extends JpaRepository<PatientObservation, Long> {
    List<PatientObservation> findByPatientId(Long patientId);
}
