package com.example.healthVault.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.healthVault.model.PatientCondition;

public interface PatientConditionRepository extends JpaRepository<PatientCondition, Long> {
    List<PatientCondition> findByPatientId(Long patientId);
}
