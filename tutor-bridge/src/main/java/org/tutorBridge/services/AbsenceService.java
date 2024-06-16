package org.tutorBridge.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.repositories.AbsenceRepo;
import org.tutorBridge.repositories.AvailabilityRepo;
import org.tutorBridge.repositories.ReservationRepo;
import org.tutorBridge.repositories.TutorRepo;
import org.tutorBridge.entities.Absence;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.validation.ValidationException;

import java.time.LocalDateTime;

@Service
public class AbsenceService extends AbstractService {
    private final AbsenceRepo absenceRepo;
    private final AvailabilityRepo availabilityRepo;
    private final ReservationRepo reservationRepo;
    private final TutorRepo tutorRepo;

    public AbsenceService(AbsenceRepo absenceRepo, AvailabilityRepo availabilityRepo, ReservationRepo reservationRepo, TutorRepo tutorRepo) {
        this.absenceRepo = absenceRepo;
        this.availabilityRepo = availabilityRepo;
        this.reservationRepo = reservationRepo;
        this.tutorRepo = tutorRepo;
    }

    @Transactional
    public void addAbsence(String email, LocalDateTime start, LocalDateTime end) {
        Tutor tutor = tutorRepo.findByEmail(email).orElseThrow(() -> new ValidationException("Tutor not found"));

        if (tutorRepo.hasConflictingAbsence(tutor, start, end)) {
            throw new ValidationException("Tutor already has an absence that conflicts with the new one.");
        }
        Absence absence = new Absence(tutor, start, end);
        absenceRepo.save(absence);
        availabilityRepo.deleteAvailabilitiesFor(tutor, start, end);
        reservationRepo.cancelReservationsFor(tutor, start, end);
    }
}
