package org.tutorBridge.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dto.AbsenceDTO;
import org.tutorBridge.dto.TimeFrameDTO;
import org.tutorBridge.entities.Absence;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.repositories.AbsenceRepo;
import org.tutorBridge.repositories.AvailabilityRepo;
import org.tutorBridge.repositories.ReservationRepo;
import org.tutorBridge.repositories.TutorRepo;
import org.tutorBridge.validation.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<AbsenceDTO> addAbsence(String email, LocalDateTime start, LocalDateTime end) {
        Tutor tutor = tutorRepo.findByEmail(email).orElseThrow(() -> new ValidationException("Tutor not found"));

        if (tutorRepo.hasConflictingAbsence(tutor, start, end)) {
            throw new ValidationException("Tutor already has an absence that conflicts with the new one.");
        }

        Absence absence = new Absence(tutor, start, end);
        absenceRepo.save(absence);
        availabilityRepo.deleteAvailabilitiesFor(tutor, start, end);
        reservationRepo.cancelReservationsFor(tutor, start, end);

        return absenceRepo
                .fetchAbsences(tutor)
                .stream()
                .map(a -> new AbsenceDTO(a.getAbsenceId(), a.getStartDate(), a.getEndDate()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AbsenceDTO> getAbsences(String email, TimeFrameDTO timeFrame) {
        Tutor tutor = tutorRepo.findByEmail(email).orElseThrow(() -> new ValidationException("Tutor not found"));
        TimeFrameDTO.fillInEmptyFields(timeFrame);
        return getAbsencesDTOS(tutor, timeFrame.getStart(), timeFrame.getEnd());
    }

    @Transactional
    public List<AbsenceDTO> deleteAbsence(String email, Long absenceId) {
        Tutor tutor = tutorRepo.findByEmail(email).orElseThrow(() -> new ValidationException("Tutor not found"));
        Absence absence = absenceRepo.findById(absenceId)
                .orElseThrow(() -> new ValidationException("Absence not found"));
        if (!absence.getTutor().equals(tutor)) {
            throw new ValidationException("Absence does not belong to tutor");
        }

        absenceRepo.delete(absence);

        return getAbsencesDTOS(tutor);
    }


    private List<AbsenceDTO> getAbsencesDTOS(Tutor tutor) {
        return absenceRepo.fetchAbsences(tutor,
                        TimeFrameDTO.getDefaultStart(),
                        TimeFrameDTO.getDefaultEnd())
                .stream()
                .map(a -> new AbsenceDTO(a.getAbsenceId(), a.getStartDate(), a.getEndDate()))
                .collect(Collectors.toList());
    }

    private List<AbsenceDTO> getAbsencesDTOS(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        return absenceRepo.fetchAbsences(tutor, start, end)
                .stream()
                .map(a -> new AbsenceDTO(a.getAbsenceId(), a.getStartDate(), a.getEndDate()))
                .collect(Collectors.toList());
    }
}
