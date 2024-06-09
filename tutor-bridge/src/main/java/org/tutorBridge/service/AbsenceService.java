package org.tutorBridge.service;

import org.tutorBridge.config.DB;
import org.tutorBridge.dao.AbsenceDao;
import org.tutorBridge.dao.ReservationDao;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.entities.Absence;
import org.tutorBridge.entities.Reservation;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.entities.enums.ReservationStatus;
import org.tutorBridge.validation.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

public class AbsenceService extends AbstractService {
    private final AbsenceDao absenceDao = new AbsenceDao();
    private final ReservationDao reservationDao = new ReservationDao();
    private final TutorDao tutorDao = new TutorDao();

    public void addAbsence(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        DB.inTransaction(em -> {
            if (tutorDao.hasConflictingAbsence(tutor, start, end, em)) {
                throw new ValidationException("Tutor already has an absence that conflicts with the new one.");
            }
            Absence absence = new Absence(tutor, start, end);
            absenceDao.save(absence, em);
            List<Reservation> affectedReservations = tutorDao.findReservationsAffectedByAbsence(tutor, start, end, em);
            for (Reservation reservation : affectedReservations) {
                reservation.setStatus(ReservationStatus.CANCELLED);
                reservationDao.update(reservation, em);
            }
        });
    }
}
