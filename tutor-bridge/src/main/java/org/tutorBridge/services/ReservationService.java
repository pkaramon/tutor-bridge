package org.tutorBridge.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dao.ReservationDao;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.dto.StatusChangeDTO;
import org.tutorBridge.entities.Reservation;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.entities.enums.ReservationStatus;
import org.tutorBridge.validation.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ReservationService extends AbstractService {
    private final ReservationDao reservationDao ;
    private final TutorDao tutorDao ;

    public ReservationService(ReservationDao reservationDao, TutorDao tutorDao) {
        this.reservationDao = reservationDao;
        this.tutorDao = tutorDao;
    }

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


    @Transactional
    public void changeReservationStatus(String email, List<StatusChangeDTO> statusChanges) {
        Tutor tutor = tutorDao.findByEmail(email).orElseThrow(() -> new ValidationException("Tutor not found"));
        List<Long> reservationIds = statusChanges.stream().map(StatusChangeDTO::getReservationId).toList();
        List<Reservation> reservations = reservationDao.findReservationsByTutorAndIds(tutor, reservationIds);
        if (reservations.size() != reservationIds.size()) {
            throw new ValidationException("Some reservations do not belong to the tutor");
        }

        IntStream.range(0, statusChanges.size()).forEach(i -> {
            Reservation reservation = reservations.get(i);
            StatusChangeDTO statusChange = statusChanges.get(i);
            if (reservation.getStatus() == ReservationStatus.CANCELLED
                    && statusChange.getStatus() != ReservationStatus.CANCELLED) {
                throw new ValidationException("Cannot change status of a reservation that is already cancelled");
            }
        });

        for (StatusChangeDTO statusChange : statusChanges) {
            reservationDao.changeStatus(statusChange.getReservationId(), statusChange.getStatus());
        }
    }


}
