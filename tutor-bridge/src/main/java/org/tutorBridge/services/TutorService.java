package org.tutorBridge.services;

import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dto.*;
import org.tutorBridge.entities.Availability;
import org.tutorBridge.entities.Specialization;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.repositories.AvailabilityRepo;
import org.tutorBridge.repositories.SpecializationRepo;
import org.tutorBridge.repositories.TutorRepo;
import org.tutorBridge.repositories.UserRepo;
import org.tutorBridge.validation.ValidationException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TutorService extends UserService<Tutor> {
    private final TutorRepo tutorRepo;
    private final AvailabilityRepo availabilityRepo;
    private final SpecializationRepo specializationRepo;

    public TutorService(TutorRepo tutorRepo, AvailabilityRepo availabilityRepo, UserRepo userDao, PasswordEncoder pe, SpecializationRepo specializationRepo) {
        super(userDao, pe);
        this.tutorRepo = tutorRepo;
        this.availabilityRepo = availabilityRepo;
        this.specializationRepo = specializationRepo;
    }


    @Transactional
    public void registerTutor(Tutor tutor) {
        for (Specialization specialization : tutor.getSpecializations()) {
            Optional<Specialization> existingSpecialization = specializationRepo.findByName(specialization.getName());
            if (existingSpecialization.isPresent()) {
                specialization.setSpecializationId(existingSpecialization.get().getSpecializationId());
            } else {
                specializationRepo.save(specialization);
            }
        }
        registerUser(tutor);
    }


    public Set<Specialization> getSpecializations(String email) {
        return tutorRepo.findByEmail(email)
                .map(Tutor::getSpecializations)
                .orElseThrow(() -> new ValidationException("Tutor not found"));
    }


    @Transactional
    public List<AvailabilityDTO> addWeeklyAvailability(String email, WeeklySlotsDTO availData) {
        Tutor tutor = tutorRepo.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Tutor not found"));


        addWeeklyAvailability(tutor, availData);

        return availabilityRepo.fetchAvailabilities(
                        tutor,
                        availData.getStartDate().atStartOfDay(),
                        availData.getEndDate().plusDays(1).atStartOfDay())
                .stream()
                .map(a -> new AvailabilityDTO(a.getAvailabilityId(), a.getStartDateTime(), a.getEndDateTime()))
                .collect(Collectors.toList());
    }

    private void addWeeklyAvailability(Tutor tutor, WeeklySlotsDTO weeklySlotsDTO) {
        LocalDate startDate = weeklySlotsDTO.getStartDate();
        LocalDate endDate = weeklySlotsDTO.getEndDate();
        Map<DayOfWeek, List<TimeRangeDTO>> weeklyTimeRanges = weeklySlotsDTO.getWeeklyTimeRanges();

        availabilityRepo.deleteAvailabilitiesFor(
                tutor,
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay());

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            Set<TimeRangeDTO> timeRanges = Set.copyOf(weeklyTimeRanges.getOrDefault(dayOfWeek, List.of()));
            for (var timeRange : timeRanges) {
                createNewAvailabilityFromTimeRange(tutor, timeRange, date);
            }
        }
    }


    private void createNewAvailabilityFromTimeRange(Tutor tutor, TimeRangeDTO timeRange, LocalDate date) {
        if (timeRange.getStart() != null && timeRange.getEnd() != null) {
            LocalDateTime startDateTime = LocalDateTime.of(date, timeRange.getStart());
            LocalDateTime endDateTime = LocalDateTime.of(date, timeRange.getEnd());
            Availability availability = new Availability(tutor, startDateTime, endDateTime);
            availabilityRepo.save(availability);
        }
    }


    @Transactional
    public void addOneTmeAvailability(Tutor tutor, LocalDateTime start, LocalDateTime end, EntityManager em) {
        if (start.isAfter(end)) {
            throw new ValidationException("Start time must be before getEnd time");
        }

        checkForAvailabilityConflicts(tutor, start, end);
        var availability = new Availability(tutor, start, end);
        availabilityRepo.save(availability);
    }

    private void checkForAvailabilityConflicts(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        if (tutorRepo.hasTutorConflictingAvailability(tutor, start, end)) {
            throw new ValidationException("Tutor already has an availability that conflicts with this one");
        }
    }

    @Override
    protected void saveUser(Tutor user) {
        tutorRepo.save(user);
    }

    @Transactional
    public TutorUpdateDTO updateTutorInfo(String email, TutorUpdateDTO tutorData) {
        Tutor tutor = tutorRepo.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Tutor not found"));

        if (tutorData.getFirstName() != null) tutor.setFirstName(tutorData.getFirstName());
        if (tutorData.getLastName() != null) tutor.setLastName(tutorData.getLastName());
        if (tutorData.getPhone() != null) tutor.setPhone(tutorData.getPhone());
        if (tutorData.getBirthDate() != null) tutor.setBirthDate(tutorData.getBirthDate());
        if (tutorData.getBio() != null) tutor.setBio(tutorData.getBio());
        if (tutorData.getSpecializations() != null) {
            Set<Specialization> specializations = getOrCreateSpecializations(tutorData.getSpecializations());
            tutor.setSpecializations(specializations);
        }

        tutorRepo.update(tutor);

        return fromTutorToDTO(tutor);
    }


    private Set<Specialization> getOrCreateSpecializations(Set<String> specializationNames) {
        return specializationNames.stream()
                .map(name -> specializationRepo.findByName(name)
                        .orElseGet(() -> {
                            Specialization newSpec = new Specialization(name);
                            specializationRepo.save(newSpec);
                            return newSpec;
                        }))
                .collect(Collectors.toSet());
    }

    public List<AvailabilityDTO> getAvailabilities(String email, TimeFrameDTO timeFrame) {
        Tutor tutor = tutorRepo.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Tutor not found"));

        return availabilityRepo.fetchAvailabilities(tutor, timeFrame.getStart(), timeFrame.getEnd())
                .stream()
                .map(a -> new AvailabilityDTO(a.getAvailabilityId(), a.getStartDateTime(), a.getEndDateTime()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TutorUpdateDTO getTutorInfo(String email) {
        Tutor tutor = tutorRepo.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Tutor not found"));

        return fromTutorToDTO(tutor);
    }


    private static TutorUpdateDTO fromTutorToDTO(Tutor tutor) {
        return new TutorUpdateDTO(
                tutor.getFirstName(),
                tutor.getLastName(),
                tutor.getPhone(),
                tutor.getBirthDate(),
                tutor.getSpecializations()
                        .stream()
                        .map(Specialization::getName)
                        .collect(Collectors.toSet()),
                tutor.getBio());
    }
}
