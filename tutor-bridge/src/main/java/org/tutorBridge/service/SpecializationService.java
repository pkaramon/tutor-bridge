package org.tutorBridge.service;

import org.tutorBridge.config.DB;
import org.tutorBridge.dao.SpecializationDao;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.entities.Specialization;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.validation.ValidationException;

import java.util.Collection;

public class SpecializationService extends AbstractService {
    SpecializationDao specializationDao = new SpecializationDao();
    TutorDao tutorDao = new TutorDao();

    public void addSpecialization(String name) {
        DB.inTransaction(em -> {
            Specialization specialization = new Specialization(name);
            specializationDao.save(specialization, em);
        });
    }

    public void removeSpecialization(String name) {
        DB.inTransaction(em -> {
            Specialization specialization = specializationDao
                    .findByName(name, em)
                    .orElseThrow(() -> new ValidationException("Specialization not found"));
            specializationDao.delete(specialization, em);
        });
    }

    public void assignSpecializations(Tutor tutor, Collection<Long> specializationIds) {
        DB.inTransaction(em -> {
            for (Long id : specializationIds) {
                Specialization specialization = specializationDao
                        .findById(id, em)
                        .orElseThrow(() -> new ValidationException("Specialization not found"));

                tutor.addSpecialization(specialization);
            }
            tutorDao.update(tutor, em);
        });
    }
}
