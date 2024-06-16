package org.tutorBridge.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dto.StatusChangeDTO;
import org.tutorBridge.entities.Reservation;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.entities.enums.ReservationStatus;
import org.tutorBridge.repositories.ReservationRepo;
import org.tutorBridge.repositories.TutorRepo;
import org.tutorBridge.validation.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ReservationService extends AbstractService {
    private final ReservationRepo reservationRepo;
    private final TutorRepo tutorRepo;

    public ReservationService(ReservationRepo reservationRepo, TutorRepo tutorRepo) {
        this.reservationRepo = reservationRepo;
        this.tutorRepo = tutorRepo;
    }

    @Transactional
    public void makeReservation(Reservation reservation) {
        Tutor tutor = reservation.getTutor();
        LocalDateTime start = reservation.getStartDateTime();
        LocalDateTime end = reservation.getEndDateTime();

        if (!tutorRepo.isTutorAvailable(tutor, start, end)) {
            throw new ValidationException("Tutor is not available at the requested time.");
        }
        if (tutorRepo.hasAbsenceDuring(tutor, start, end)) {
            throw new ValidationException("Tutor has an absence record for the requested time.");
        }
        if (tutorRepo.hasConflictingReservation(tutor, start, end)) {
            throw new ValidationException("Tutor has another reservation at the requested time.");
        }
        reservationRepo.save(reservation);
    }


    @Transactional
    public void changeReservationStatus(Tutor tutor, List<StatusChangeDTO> statusChanges) {
        List<Long> reservationIds = statusChanges.stream().map(StatusChangeDTO::getReservationId).toList();
        List<Reservation> reservations = reservationRepo.findReservationsByTutorAndIds(tutor, reservationIds);
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
            reservationRepo.changeStatus(statusChange.getReservationId(), statusChange.getStatus());
        }
    }


}
