package org.tutorBridge.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.tutorBridge.dto.*;
import org.tutorBridge.entities.Specialization;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.services.AbsenceService;
import org.tutorBridge.services.ReservationService;
import org.tutorBridge.services.TutorService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tutors")
public class TutorController {
    private final TutorService tutorService;
    private final AbsenceService absenceService;
    private final ReservationService reservationService;

    public TutorController(TutorService tutorService, AbsenceService absenceService, ReservationService reservationService) {
        this.tutorService = tutorService;
        this.absenceService = absenceService;
        this.reservationService = reservationService;
    }

    @PostMapping("/register")
    public Map<String, String> registerTutor(@Valid @RequestBody TutorRegisterDTO tutorData) {
        Tutor tutor = new Tutor(
                tutorData.getFirstName(),
                tutorData.getLastName(),
                tutorData.getPhone(),
                tutorData.getEmail(),
                tutorData.getPassword(),
                tutorData.getBio(),
                tutorData.getBirthDate()
        );
        tutor.setSpecializations(
                tutorData.getSpecializations().stream()
                        .map(Specialization::new)
                        .collect(Collectors.toSet())
        );
        tutorService.registerTutor(tutor);
        return Collections.singletonMap("message", "Tutor registered successfully");
    }

    @GetMapping("/specializations")
    public TutorSpecializationDTO getTutorSpecializations(Authentication authentication) {
        String email = authentication.getName();
        Set<String> specializations = tutorService.getSpecializations(email).stream()
                .map(Specialization::getName)
                .collect(Collectors.toSet());
        return new TutorSpecializationDTO(specializations);
    }


    @PostMapping("/update")
    public Map<String, String> updateTutor(@Valid @RequestBody TutorUpdateDTO tutorData, Authentication authentication) {
        String email = authentication.getName();
        tutorService.updateTutorInfo(email, tutorData);
        return Collections.singletonMap("message", "Tutor information updated successfully");
    }

    @PostMapping("/availability/weekly")
    public List<AvailabilityDTO> setWeeklyAvailability(@Valid @RequestBody WeeklySlotsDTO weeklySlotsDTO,
                                                       Authentication authentication) {
        String email = authentication.getName();
        return tutorService.addWeeklyAvailability(email, weeklySlotsDTO);
    }

    @PostMapping("/absences")
    public Map<String, String> addAbsence(@Valid @RequestBody AbsenceDTO absenceDTO, Authentication authentication) {
        String email = authentication.getName();
        absenceService.addAbsence(email, absenceDTO.getStartDate(), absenceDTO.getEndDate());
        return Collections.singletonMap("message", "Absence added successfully");
    }

    @PostMapping("/reservations/status")
    public Map<String, String> changeReservationsStatus(@Valid @RequestBody StatusChangesDTO changes,
                                                        Authentication authentication) {
        String email = authentication.getName();
        reservationService.changeReservationStatus(email, changes.getChanges());
        return Collections.singletonMap("message", "Reservation status changed successfully");
    }

}
