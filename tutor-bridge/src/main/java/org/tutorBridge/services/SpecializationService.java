package org.tutorBridge.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dao.SpecializationDao;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.dao.UserDao;
import org.tutorBridge.entities.Specialization;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.validation.ValidationException;

import java.util.Set;
import java.util.stream.Collectors;


@Service
public class SpecializationService extends AbstractService {
    private final SpecializationDao specializationDao;
    private final TutorDao tutorDao;
    private final UserDao userDao;

    public SpecializationService(SpecializationDao specializationDao, TutorDao tutorDao, UserDao userDao) {
        this.specializationDao = specializationDao;
        this.tutorDao = tutorDao;
        this.userDao = userDao;
    }

    public void addSpecialization(String name) {
        Specialization specialization = new Specialization(name);
        specializationDao.save(specialization);
    }

    @Transactional
    public void updateTutorSpecializations(String email, Set<String> specializationNames) {
        Tutor tutor = (Tutor) userDao.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Tutor not found"));

        Set<Specialization> specializations = getOrCreateSpecializations(specializationNames);
        tutor.setSpecializations(specializations);
        tutorDao.update(tutor);
    }

    private Set<Specialization> getOrCreateSpecializations(Set<String> specializationNames) {
        return specializationNames.stream()
                .map(name -> specializationDao.findByName(name)
                        .orElseGet(() -> {
                            Specialization newSpec = new Specialization(name);
                            specializationDao.save(newSpec);
                            return newSpec;
                        }))
                .collect(Collectors.toSet());
    }


}
