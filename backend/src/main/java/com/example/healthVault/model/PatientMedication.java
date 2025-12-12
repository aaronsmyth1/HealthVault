package com.example.healthVault.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient_medications")
public class PatientMedication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private String fhirId;
    private String medicationDisplay;
    private String medicationCode;
    private String dosageText;
    private String status;
    private String authoredOn;
    private String intent;

    public PatientMedication() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public String getFhirId() { return fhirId; }
    public void setFhirId(String fhirId) { this.fhirId = fhirId; }

    public String getMedicationDisplay() { return medicationDisplay; }
    public void setMedicationDisplay(String medicationDisplay) { this.medicationDisplay = medicationDisplay; }

    public String getMedicationCode() { return medicationCode; }
    public void setMedicationCode(String medicationCode) { this.medicationCode = medicationCode; }

    public String getDosageText() { return dosageText; }
    public void setDosageText(String dosageText) { this.dosageText = dosageText; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAuthoredOn() { return authoredOn; }
    public void setAuthoredOn(String authoredOn) { this.authoredOn = authoredOn; }

    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }
}
