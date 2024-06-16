package org.tutorBridge.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.repositories.SpecializationRepo;
import org.tutorBridge.repositories.TutorRepo;
import org.tutorBridge.entities.Specialization;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.repositories.UserRepo;
import org.tutorBridge.validation.ValidationException;

import java.util.Set;
import java.util.stream.Collectors;


@Service
public class SpecializationService extends AbstractService {
    private final SpecializationRepo specializationRepo;
    private final TutorRepo tutorRepo;
    private final UserRepo userRepo;

    public SpecializationService(SpecializationRepo specializationRepo, TutorRepo tutorRepo, UserRepo userRepo) {
        this.specializationRepo = specializationRepo;
        this.tutorRepo = tutorRepo;
        this.userRepo = userRepo;
    }

    public void addSpecialization(String name) {
        Specialization specialization = new Specialization(name);
        specializationRepo.save(specialization);
    }

    @Transactional
    public void updateTutorSpecializations(String email, Set<String> specializationNames) {
        Tutor tutor = (Tutor) userRepo.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Tutor not found"));

        Set<Specialization> specializations = getOrCreateSpecializations(specializationNames);
        tutor.setSpecializations(specializations);
        tutorRepo.update(tutor);
    }

    private Set<Specialization> getOrCreateSpecializations(Set<String> specializationNames) {
        return specializationNames.stream()
                .map(name -> specializationRepo.findByName(name)
                        .orElseGet(() -> {
                            Specialization newSpec = new Specialization(name);
                            specializationRepo.save(newSpec);
                            return newSpec;
                        }))
                .collect(Collectors.toSet());
    }


}
