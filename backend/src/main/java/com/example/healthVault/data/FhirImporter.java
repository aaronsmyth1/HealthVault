package com.example.healthVault.data;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.healthVault.model.Patient;
import com.example.healthVault.model.PatientCondition;
import com.example.healthVault.repository.PatientConditionRepository;
import com.example.healthVault.repository.PatientRepository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

@Component
public class FhirImporter implements CommandLineRunner {

    private final PatientRepository patientRepo;
    private final PatientConditionRepository conditionRepo;

    private final FhirContext ctx = FhirContext.forR4();
    private final Set<String> importedIds = new HashSet<>();
    private final Map<String, org.hl7.fhir.r4.model.Patient> patientsByFhirId = new HashMap<>();

    public FhirImporter(
            PatientRepository patientRepo,
            PatientConditionRepository conditionRepo) {
        this.patientRepo = patientRepo;
        this.conditionRepo = conditionRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        URL resourceUrl = getClass().getClassLoader().getResource("fhir");
        Path folder;
        
        if (resourceUrl != null) {
            try {
                folder = Paths.get(resourceUrl.toURI());
            } catch (Exception e) {
                folder = Paths.get("src/main/resources/fhir");
            }
        } else {
            folder = Paths.get("src/main/resources/fhir");
        }

        if (!Files.exists(folder)) {
            System.out.println("FHIR folder not found: " + folder.toAbsolutePath());
            return;
        }

        try (Stream<Path> files = Files.list(folder)) {
            files.filter(f -> f.toString().endsWith(".json"))
                 .forEach(this::importFhirFile);
        }

        System.out.println("=== All Patient Identifiers in DB ===");
        patientRepo.findAll().forEach(p -> System.out.println(p.getIdentifier()));
    }

    private void importFhirFile(Path file) {
        try {
            String jsonText = Files.readString(file);
            IParser parser = ctx.newJsonParser();
            var resource = parser.parseResource(jsonText);

            if(resource instanceof org.hl7.fhir.r4.model.Patient fhirPatient) {
                savePatient(fhirPatient);
            } else if (resource instanceof Bundle bundle) {
                //Save all patients
                for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
                    if (entry.getResource() instanceof org.hl7.fhir.r4.model.Patient p) {
                        savePatient(p);
                        String patientId = extractIdFromReference(p.getId());
                        System.out.println("Mapping FHIR Patient ID to map: " + patientId);
                        patientsByFhirId.put(patientId, p);
                    }
                }
                
                // Save related resources from patient (e.g., Conditions)
                for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
                    Resource res = entry.getResource();
                    if (res instanceof Condition condition) {
                        saveCondition(condition);
                    }
                }
            } else {
                System.out.println("Skipping non-patient/bundle resource in file: " + file.getFileName());
            }

        } catch (IOException e) {
            System.out.println("Failed to read file: " + file + " -> " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Failed parsing FHIR JSON for file: " + file + " -> " + e.getMessage());
        }
    }

    private void savePatient(org.hl7.fhir.r4.model.Patient fhirPatient) {
        List<Identifier> identifiers = fhirPatient.getIdentifier();
        if (identifiers.isEmpty()) return;

        Identifier primaryId = identifiers.get(0);
        Optional<Identifier> mrn = identifiers.stream()
                .filter(id -> id.hasType() && "MRN".equalsIgnoreCase(id.getType().getText()))
                .findFirst();
        if (mrn.isPresent()) primaryId = mrn.get();

        String identifier = primaryId.getValue();

        if (importedIds.contains(identifier) || patientRepo.findByIdentifier(identifier).isPresent()) {
            return;
        }

        importedIds.add(identifier);

        Patient p = new Patient();
        p.setIdentifier(identifier);

        List<HumanName> names = fhirPatient.getName();
        if (!names.isEmpty()) {
            HumanName name = names.get(0);
            if (name.hasGiven()) p.setGivenName(name.getGivenAsSingleString());
            if (name.hasFamily()) p.setFamilyName(name.getFamily());
        }

        if (fhirPatient.hasGender()) p.setGender(fhirPatient.getGender().toCode());
        if (fhirPatient.hasBirthDate()) p.setBirthDate(fhirPatient.getBirthDate().toString());

        List<Address> addresses = fhirPatient.getAddress();
        if (!addresses.isEmpty()) {
            Address addr = addresses.get(0);
            if (!addr.getLine().isEmpty()) p.setAddressLine(addr.getLine().get(0).toString());
            if (addr.hasCity()) p.setCity(addr.getCity());
            if (addr.hasPostalCode()) p.setPostalCode(addr.getPostalCode());
        }

        for (ContactPoint t : fhirPatient.getTelecom()) {
            switch (t.getSystem()) {
                case PHONE -> p.setPhone(t.getValue());
                case EMAIL -> p.setEmail(t.getValue());
                default -> {}
            }
        }

        if (fhirPatient.hasManagingOrganization())
            p.setManagingOrganisation(fhirPatient.getManagingOrganization().getReference());
        if (!fhirPatient.getGeneralPractitioner().isEmpty())
            p.setGeneralPractitioner(fhirPatient.getGeneralPractitionerFirstRep().getReference());

        patientRepo.save(p);
        System.out.println("Imported Patient: " + identifier);
    }

    private void saveCondition(Condition condition) {
        if (!condition.hasSubject()) {
            System.out.println("Condition has no subject, skipping");
            return;
        }

        String patientRef = condition.getSubject().getReference();
        String patientId = extractIdFromReference(patientRef);
        System.out.println("Processing Condition - Patient Ref: " + patientRef + " -> ID: " + patientId);
        
        org.hl7.fhir.r4.model.Patient fhirPatient = patientsByFhirId.get(patientId);
        if (fhirPatient == null) {
            System.out.println("  FHIR Patient not found in map for ID: " + patientId);
            return;
        }

        String identifier = fhirPatient.getIdentifier().get(0).getValue();
        System.out.println("  FHIR Patient identifier: " + identifier);
        
        Optional<Patient> patient = patientRepo.findByIdentifier(identifier);
        if (patient.isEmpty()) {
            System.out.println("  DB Patient not found for identifier: " + identifier);
            return;
        }

        System.out.println("  Found DB Patient ID: " + patient.get().getId() + ", saving condition...");
        
        PatientCondition pc = new PatientCondition();
        pc.setPatient(patient.get());
        pc.setFhirId(condition.getId());
        
        if (condition.hasCode() && condition.getCode().hasCoding()) {
            Coding coding = condition.getCode().getCodingFirstRep();
            pc.setCode(coding.getCode());
            pc.setDisplay(coding.getDisplay());
        }
        
        if (condition.hasRecordedDate()) {
            pc.setRecordedDate(condition.getRecordedDate().toString());
        }
        
        if (condition.hasClinicalStatus()) {
            pc.setClinicalStatus(condition.getClinicalStatus().getCodingFirstRep().getDisplay());
        }
        
        if (condition.hasVerificationStatus()) {
            pc.setVerificationStatus(condition.getVerificationStatus().getCodingFirstRep().getDisplay());
        }

        conditionRepo.save(pc);
        System.out.println("  Condition saved successfully!");
    }


    private String extractIdFromReference(String reference) {
        // Reference format: "Patient/825e3859-..." or "urn:uuid:825e3859-..."
        if (reference.contains("/")) {
            return reference.substring(reference.lastIndexOf("/") + 1);
        }
        if (reference.contains(":")) {
            return reference.substring(reference.lastIndexOf(":") + 1);
        }
        return reference;
    }
}
