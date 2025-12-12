package com.example.healthVault.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient_observations")
public class PatientObservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private String fhirId;
    private String code;
    private String display;
    private String valueString;
    private String valueQuantity;
    private String unit;
    private String effectiveDateTime;
    private String status;

    public PatientObservation() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public String getFhirId() { return fhirId; }
    public void setFhirId(String fhirId) { this.fhirId = fhirId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDisplay() { return display; }
    public void setDisplay(String display) { this.display = display; }

    public String getValueString() { return valueString; }
    public void setValueString(String valueString) { this.valueString = valueString; }

    public String getValueQuantity() { return valueQuantity; }
    public void setValueQuantity(String valueQuantity) { this.valueQuantity = valueQuantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getEffectiveDateTime() { return effectiveDateTime; }
    public void setEffectiveDateTime(String effectiveDateTime) { this.effectiveDateTime = effectiveDateTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
