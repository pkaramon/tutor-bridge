package org.tutorBridge.service;

import org.tutorBridge.config.DB;
import org.tutorBridge.dao.AvailabilityDao;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.entities.Availability;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.validation.ValidationException;

import jakarta.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class TutorService extends UserService<Tutor> {
    private final TutorDao tutorDao = new TutorDao();
    private final AvailabilityDao availabilityDao = new AvailabilityDao();

    public void registerTutor(Tutor tutor) {
        registerUser(tutor);
    }

    public void updateTutor(Tutor tutor) {
        tutorDao.update(tutor);
    }

    public void addWeeklyAvailability(Tutor tutor, Map<DayOfWeek, List<TimeRange>> weeklyTimeRanges, int weeksInAdvance) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusWeeks(weeksInAdvance);

        DB.inTransaction((em) -> {
            var existingSlots = tutorDao.fetchAvailabilities(tutor, startDate, endDate, em);
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                List<TimeRange> timeRanges = weeklyTimeRanges.getOrDefault(dayOfWeek, List.of());
                for (TimeRange timeRange : timeRanges) {
                    createNewAvailabilityFromTimeRange(tutor, timeRange, date, existingSlots, em);
                }
            }
        });
    }


    private void createNewAvailabilityFromTimeRange(Tutor tutor, TimeRange timeRange, LocalDate date,
                                                    List<Availability> existingAvailabilities,
                                                    EntityManager em) {
        if (timeRange.start() != null && timeRange.end() != null) {
            LocalDateTime startDateTime = LocalDateTime.of(date, timeRange.start());
            LocalDateTime endDateTime = LocalDateTime.of(date, timeRange.end());

            if (hasConflict(existingAvailabilities, startDateTime, endDateTime)) {
                throw new ValidationException("Tutor already has an availability that conflicts with the new one");
            }

            Availability availability = new Availability(tutor, startDateTime, endDateTime);
            availabilityDao.save(availability, em);
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


    public void addOneTimeAvailability(Tutor tutor, LocalDateTime start, LocalDateTime end, EntityManager em) {
        if (start.isAfter(end)) {
            throw new ValidationException("Start time must be before end time");
        }

        checkForAvailabilityConflicts(tutor, start, end, em);
        var availability = new Availability(tutor, start, end);
        availabilityDao.save(availability);
    }

    private void checkForAvailabilityConflicts(Tutor tutor, LocalDateTime start, LocalDateTime end, EntityManager em) {
        if (tutorDao.hasTutorConflictingAvailability(tutor, start, end, em)) {
            throw new ValidationException("Tutor already has an availability that conflicts with this one");
        }
    }

    @Override
    protected void saveUser(Tutor user, EntityManager em) {
        tutorDao.save(user, em);
    }
}
