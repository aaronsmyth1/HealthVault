package com.example.healthVault.dto;

import java.util.List;

import com.example.healthVault.model.Patient;
import com.example.healthVault.model.PatientCondition;

public class PatientDTO {
    private Long id;
    private String identifier;
    private String givenName;
    private String familyName;
    private String gender;
    private String birthDate;
    private String phone;
    private String email;
    private String addressLine;
    private String city;
    private String postalCode;
    private String managingOrganisation;
    private String generalPractitioner;

    private List<PatientCondition> conditions;

    public PatientDTO() {}

    public PatientDTO(Patient patient) {
        this.id = patient.getId();
        this.identifier = patient.getIdentifier();
        this.givenName = patient.getGivenName();
        this.familyName = patient.getFamilyName();
        this.gender = patient.getGender();
        this.birthDate = patient.getBirthDate();
        this.phone = patient.getPhone();
        this.email = patient.getEmail();
        this.addressLine = patient.getAddressLine();
        this.city = patient.getCity();
        this.postalCode = patient.getPostalCode();
        this.managingOrganisation = patient.getManagingOrganisation();
        this.generalPractitioner = patient.getGeneralPractitioner();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getGivenName() { return givenName; }
    public void setGivenName(String givenName) { this.givenName = givenName; }

    public String getFamilyName() { return familyName; }
    public void setFamilyName(String familyName) { this.familyName = familyName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getManagingOrganisation() { return managingOrganisation; }
    public void setManagingOrganisation(String managingOrganisation) { this.managingOrganisation = managingOrganisation; }

    public String getGeneralPractitioner() { return generalPractitioner; }
    public void setGeneralPractitioner(String generalPractitioner) { this.generalPractitioner = generalPractitioner; }

    public List<PatientCondition> getConditions() { return conditions; }
    public void setConditions(List<PatientCondition> conditions) { this.conditions = conditions; }

}
