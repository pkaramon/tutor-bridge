package org.tutorBridge.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.tutorBridge.dto.*;
import org.tutorBridge.entities.Specialization;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.services.AbsenceService;
import org.tutorBridge.services.PlanService;
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
    private final PlanService planService;

    public TutorController(TutorService tutorService, AbsenceService absenceService, ReservationService reservationService, PlanService planService) {
        this.tutorService = tutorService;
        this.absenceService = absenceService;
        this.reservationService = reservationService;
        this.planService = planService;
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


    @PutMapping("/account")
    public TutorUpdateDTO updateTutor(@Valid @RequestBody TutorUpdateDTO tutorData, Authentication authentication) {
        String email = authentication.getName();
        return tutorService.updateTutorInfo(email, tutorData);
    }

    @GetMapping("/account")
    public TutorUpdateDTO getTutorInfo(Authentication authentication) {
        String email = authentication.getName();
        return tutorService.getTutorInfo(email);
    }

    @GetMapping("/availability")
    public List<AvailabilityDTO> getAvailabilities(@RequestBody(required = false) TimeFrameDTO timeframe,
                                                   Authentication authentication) {
        timeframe = TimeFrameDTO.fillInEmptyFields(timeframe);
        String email = authentication.getName();
        return tutorService.getAvailabilities(email, timeframe);
    }


    @PutMapping("/availability")
    public List<AvailabilityDTO> setWeeklyAvailability(@Valid @RequestBody WeeklySlotsDTO weeklySlotsDTO,
                                                       Authentication authentication) {
        String email = authentication.getName();
        return tutorService.addWeeklyAvailability(email, weeklySlotsDTO);
    }

    @PostMapping("/absence")
    public List<AbsenceDTO> addAbsence(@RequestBody @Valid AbsenceDTO absenceDTO, Authentication authentication) {
        String email = authentication.getName();
        return absenceService.addAbsence(email, absenceDTO.getStart(), absenceDTO.getEnd());
    }

    @DeleteMapping("/absence/{absenceId}")
    public List<AbsenceDTO> deleteAbsence(@PathVariable(name = "absenceId") Long absenceId,
                                          Authentication authentication) {
        String email = authentication.getName();
        return absenceService.deleteAbsence(email, absenceId);
    }

    @GetMapping("/absence")
    public List<AbsenceDTO> getAbsences(@RequestBody(required = false) TimeFrameDTO timeframe,
                                        Authentication authentication) {
        timeframe = TimeFrameDTO.fillInEmptyFields(timeframe);
        String email = authentication.getName();
        return absenceService.getAbsences(email, timeframe);
    }


    @PostMapping("/reservations/status")
    public Map<String, String> changeReservationsStatus(@Valid @RequestBody StatusChangesDTO changes,
                                                        Authentication authentication) {
        String email = authentication.getName();
        reservationService.changeReservationStatus(email, changes.getChanges());
        return Collections.singletonMap("message", "Reservation status changed successfully");
    }

    @GetMapping("/plan")
    public PlanResponseDTO getPlan(Authentication authentication, TimeFrameDTO timeframe) {
        String email = authentication.getName();
        return planService.getPlanForTutor(email, timeframe);
    }
}
