package org.tutorBridge.services;

import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dao.AvailabilityDao;
import org.tutorBridge.dao.SpecializationDao;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.dao.UserDao;
import org.tutorBridge.dto.AvailabilityDTO;
import org.tutorBridge.dto.TimeRangeDTO;
import org.tutorBridge.dto.TutorUpdateDTO;
import org.tutorBridge.dto.WeeklySlotsDTO;
import org.tutorBridge.entities.Availability;
import org.tutorBridge.entities.Specialization;
import org.tutorBridge.entities.Tutor;
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
    private final TutorDao tutorDao;
    private final AvailabilityDao availabilityDao;
    private final SpecializationDao specializationDao;

    public TutorService(TutorDao tutorDao, AvailabilityDao availabilityDao, UserDao userDao, PasswordEncoder pe, SpecializationDao specializationDao) {
        super(userDao, pe);
        this.tutorDao = tutorDao;
        this.availabilityDao = availabilityDao;
        this.specializationDao = specializationDao;
    }


    @Transactional
    public void registerTutor(Tutor tutor) {
        for (Specialization specialization : tutor.getSpecializations()) {
            Optional<Specialization> existingSpecialization = specializationDao.findByName(specialization.getName());
            if (existingSpecialization.isPresent()) {
                specialization.setSpecializationId(existingSpecialization.get().getSpecializationId());
            } else {
                specializationDao.save(specialization);
            }
        }
        registerUser(tutor);
    }


    public Set<Specialization> getSpecializations(String email) {
        return tutorDao.findByEmail(email)
                .map(Tutor::getSpecializations)
                .orElseThrow(() -> new ValidationException("Tutor not found"));
    }


    @Transactional
    public List<AvailabilityDTO> addWeeklyAvailability(String email, WeeklySlotsDTO availData) {
        Tutor tutor = tutorDao.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Tutor not found"));


        addWeeklyAvailability(tutor, availData);

        return availabilityDao.fetchAvailabilities(tutor, availData.getStartDate(), availData.getEndDate())
                .stream()
                .map(a -> new AvailabilityDTO(a.getAvailabilityId(), a.getStartDateTime(), a.getEndDateTime()))
                .collect(Collectors.toList());
    }

    private void addWeeklyAvailability(Tutor tutor, WeeklySlotsDTO weeklySlotsDTO) {
        LocalDate startDate = weeklySlotsDTO.getStartDate();
        LocalDate endDate = weeklySlotsDTO.getEndDate();
        Map<DayOfWeek, List<TimeRangeDTO>> weeklyTimeRanges = weeklySlotsDTO.getWeeklyTimeRanges();

        availabilityDao.deleteAvailabilitiesFor(
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
            availabilityDao.save(availability);
        }
    }

    private boolean hasConflict(List<Availability> existingAvailabilities, LocalDateTime newStart, LocalDateTime newEnd) {
        for (Availability availability : existingAvailabilities) {
            if (availability.getStartDateTime().isBefore(newEnd) && availability.getEndDateTime().isAfter(newStart)) {
                return true;
            }
        }
        return false;
    }


    @Transactional
    public void addOneTmeAvailability(Tutor tutor, LocalDateTime start, LocalDateTime end, EntityManager em) {
        if (start.isAfter(end)) {
            throw new ValidationException("Start time must be before end time");
        }

        checkForAvailabilityConflicts(tutor, start, end);
        var availability = new Availability(tutor, start, end);
        availabilityDao.save(availability);
    }

    private void checkForAvailabilityConflicts(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        if (tutorDao.hasTutorConflictingAvailability(tutor, start, end)) {
            throw new ValidationException("Tutor already has an availability that conflicts with this one");
        }
    }

    @Override
    protected void saveUser(Tutor user) {
        tutorDao.save(user);
    }

    @Transactional
    public void updateTutorInfo(String email, TutorUpdateDTO tutorData) {
        Tutor tutor = tutorDao.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Tutor not found"));

        if (tutorData.getFirstName() != null) tutor.setFirstName(tutorData.getFirstName());
        if (tutorData.getLastName() != null) tutor.setLastName(tutorData.getLastName());
        if (tutorData.getPhone() != null) tutor.setPhone(tutorData.getPhone());
        if (tutorData.getEmail() != null) tutor.setEmail(tutorData.getEmail());
        if (tutorData.getBirthDate() != null) tutor.setBirthDate(tutorData.getBirthDate());
        if (tutorData.getBio() != null) tutor.setBio(tutorData.getBio());
        if (tutorData.getSpecializations() != null) {
            Set<Specialization> specializations = getOrCreateSpecializations(tutorData.getSpecializations());
            tutor.setSpecializations(specializations);
        }

        tutorDao.update(tutor);
    }

    private Set<Specialization> getOrCreateSpecializations(Set<String> specializationNames) {
        return specializationNames.stream()
                .map(name -> specializationDao.findByName(name)
                        .orElseGet(() -> {
                            Specialization newSpec = new Specialization(name);
                            specializationDao.save(newSpec);
                            return newSpec;
                        }))
                .collect(Collectors.toSet());
    }
}
