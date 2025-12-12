package com.example.healthVault.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
                                    //due to the nested nature of FHIR Patient resource, we flatten some fields for simplicitiy
    private String identifier;      // FHIR: identifier[0].value
    private String givenName;       // FHIR: name[0].given[0]
    private String familyName;      // FHIR: name[0].family
    private String gender;          // FHIR: gender
    private String birthDate;       // FHIR: birthDate

    private String addressLine;     // FHIR: address[0].line[0]
    private String city;            // FHIR: address[0].city
    private String postalCode;      // FHIR: address[0].postalCode

    private String phone;           // FHIR: telecom[0].value
    private String email;           // FHIR: telecom[1].value

    private String managingOrganisation;
    private String generalPractitioner; // FHIR: managingOrganization

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

    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getManagingOrganisation() { return managingOrganisation; }
    public void setManagingOrganisation(String managingOrganisation) { this.managingOrganisation = managingOrganisation; }

    public String getGeneralPractitioner() { return generalPractitioner; }
    public void setGeneralPractitioner(String generalPractitioner) { this.generalPractitioner = generalPractitioner; }

}


