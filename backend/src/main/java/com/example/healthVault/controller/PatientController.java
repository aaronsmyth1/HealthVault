package com.example.healthVault.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.healthVault.repository.PatientRepository;

@Controller
public class PatientController {

    private final PatientRepository repo;

    public PatientController(PatientRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/patients")
    public String listPatients(Model model) {
        model.addAttribute("patients", repo.findAll());
        return "patients"; 
    }
}

