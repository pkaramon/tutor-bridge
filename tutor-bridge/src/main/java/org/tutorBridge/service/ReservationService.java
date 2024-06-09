package org.tutorBridge.service;

import org.tutorBridge.config.DB;
import org.tutorBridge.dao.ReservationDao;
import org.tutorBridge.dao.StudentDao;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.entities.Reservation;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.validation.ValidationException;

import java.time.LocalDateTime;

public class ReservationService extends AbstractService {
    private final ReservationDao reservationDao = new ReservationDao();
    private final StudentDao studentDao = new StudentDao();
    private final TutorDao tutorDao = new TutorDao();

    public void makeReservation(Reservation reservation) {
        Tutor tutor = reservation.getTutor();
        LocalDateTime start = reservation.getStartDateTime();
        LocalDateTime end = reservation.getEndDateTime();

        DB.inTransaction(em -> {
            if (!tutorDao.isTutorAvailable(tutor, start, end, em)) {
                throw new ValidationException("Tutor is not available at the requested time.");
            }
            if (tutorDao.hasAbsenceDuring(tutor, start, end, em)) {
                throw new ValidationException("Tutor has an absence record for the requested time.");
            }
            if (tutorDao.hasConflictingReservation(tutor, start, end, em)) {
                throw new ValidationException("Tutor has another reservation at the requested time.");
            }
            reservationDao.save(reservation, em);
        });
    }
}
