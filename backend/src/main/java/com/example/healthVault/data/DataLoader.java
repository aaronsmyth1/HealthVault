package com.example.healthVault.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.healthVault.repository.PatientRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final PatientRepository repo;

    public DataLoader(PatientRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {

        // Patient p1 = new Patient();
        // p1.setIdentifier("p001");
        // p1.setGivenName("John");
        // p1.setFamilyName("Doe");
        // p1.setGender("male");
        // p1.setBirthDate("1990-06-12");
        // p1.setAddressLine("123 Main St");
        // p1.setCity("Dublin");
        // p1.setPostalCode("D01");
        // p1.setPhone("0871234567");
        // p1.setEmail("john@example.com");
        // p1.setGeneralPractitioner("Dr. Mary Murphy");
        // p1.setManagingOrganisation("St. Patrick's Hospital");

        // Patient p2 = new Patient();
        // p2.setIdentifier("p002");
        // p2.setGivenName("Emily");
        // p2.setFamilyName("Smith");
        // p2.setGender("female");
        // p2.setBirthDate("1985-11-20");
        // p2.setAddressLine("45 Green Lane");
        // p2.setCity("Cork");
        // p2.setPostalCode("C02");
        // p2.setPhone("0839876543");
        // p2.setEmail("emily@example.com");
        // p2.setGeneralPractitioner("Dr. Sean O'Brien");
        // p2.setManagingOrganisation("Cork General Hospital");

        // repo.save(p1);
        // repo.save(p2);

         // Print all identifiers
    System.out.println("=== All Patient Identifiers ===");
    repo.findAll().forEach(p -> System.out.println(p.getIdentifier()));


        }
    }

