package org.tutorBridge.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dao.ReservationDao;
import org.tutorBridge.dao.StudentDao;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.entities.Reservation;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.validation.ValidationException;

import java.time.LocalDateTime;

@Service
public class ReservationService extends AbstractService {
    private final ReservationDao reservationDao = new ReservationDao();
    private final StudentDao studentDao = new StudentDao();
    private final TutorDao tutorDao = new TutorDao();

    @Transactional
    public void makeReservation(Reservation reservation) {
        Tutor tutor = reservation.getTutor();
        LocalDateTime start = reservation.getStartDateTime();
        LocalDateTime end = reservation.getEndDateTime();

        if (!tutorDao.isTutorAvailable(tutor, start, end)) {
            throw new ValidationException("Tutor is not available at the requested time.");
        }
        if (tutorDao.hasAbsenceDuring(tutor, start, end)) {
            throw new ValidationException("Tutor has an absence record for the requested time.");
        }
        if (tutorDao.hasConflictingReservation(tutor, start, end)) {
            throw new ValidationException("Tutor has another reservation at the requested time.");
        }
        reservationDao.save(reservation);
    }
}
