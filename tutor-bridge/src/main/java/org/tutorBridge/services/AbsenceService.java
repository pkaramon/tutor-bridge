package org.tutorBridge.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dao.AbsenceDao;
import org.tutorBridge.dao.AvailabilityDao;
import org.tutorBridge.dao.ReservationDao;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.entities.Absence;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.validation.ValidationException;

import java.time.LocalDateTime;

@Service
public class AbsenceService extends AbstractService {
    private final AbsenceDao absenceDao;
    private final AvailabilityDao availabilityDao;
    private final ReservationDao reservationDao;
    private final TutorDao tutorDao;

    public AbsenceService(AbsenceDao absenceDao, AvailabilityDao availabilityDao, ReservationDao reservationDao, TutorDao tutorDao) {
        this.absenceDao = absenceDao;
        this.availabilityDao = availabilityDao;
        this.reservationDao = reservationDao;
        this.tutorDao = tutorDao;
    }

    @Transactional
    public void addAbsence(String email, LocalDateTime start, LocalDateTime end) {
        Tutor tutor = tutorDao.findByEmail(email).orElseThrow(() -> new ValidationException("Tutor not found"));

        if (tutorDao.hasConflictingAbsence(tutor, start, end)) {
            throw new ValidationException("Tutor already has an absence that conflicts with the new one.");
        }
        Absence absence = new Absence(tutor, start, end);
        absenceDao.save(absence);
        availabilityDao.deleteAvailabilitiesFor(tutor, start, end);
        reservationDao.cancelReservationsFor(tutor, start, end);
    }
}
