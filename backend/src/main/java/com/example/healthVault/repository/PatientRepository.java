package com.example.healthVault.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.healthVault.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByIdentifier(String identifier);
}
