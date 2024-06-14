package org.tutorBridge.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dao.SpecializationDao;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.entities.Specialization;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.validation.ValidationException;

import java.util.Collection;


@Service
public class SpecializationService extends AbstractService {
    SpecializationDao specializationDao = new SpecializationDao();
    TutorDao tutorDao = new TutorDao();

    public void addSpecialization(String name) {
        Specialization specialization = new Specialization(name);
        specializationDao.save(specialization);
    }

    @Transactional
    public void removeSpecialization(String name) {
        Specialization specialization = specializationDao
                .findByName(name)
                .orElseThrow(() -> new ValidationException("Specialization not found"));
        specializationDao.delete(specialization);
    }

    @Transactional
    public void assignSpecializations(Tutor tutor, Collection<Long> specializationIds) {
        for (Long id : specializationIds) {
            Specialization specialization = specializationDao
                    .findById(id)
                    .orElseThrow(() -> new ValidationException("Specialization not found"));

            tutor.addSpecialization(specialization);
        }
        tutorDao.update(tutor);
    }
}
