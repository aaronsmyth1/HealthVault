package com.example.healthVault.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.healthVault.model.Patient;
import com.example.healthVault.repository.PatientRepository;

@Service
public class PatientService {

    private final PatientRepository repo;

    public PatientService(PatientRepository repo) {
        this.repo = repo;
    }

    public Patient create(Patient patient) {
        return repo.save(patient);
    }

    public List<Patient> getAll() {
        return repo.findAll();
    }

    public Optional<Patient> getById(Long id) {
        return repo.findById(id);
    }
}
