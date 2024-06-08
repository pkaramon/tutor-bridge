package org.tutorBridge.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.tutorBridge.dao.AvailabilityDao;
import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.entities.Availability;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.validation.ValidationException;

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
        validateEntity(tutor);
        tutorDao.update(tutor);
    }

    public void addWeeklyAvailability(Tutor tutor, Map<DayOfWeek, List<TimeRange>> weeklyTimeRanges, int weeksInAdvance) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusWeeks(weeksInAdvance);
        List<Availability> existingAvailabilities = tutorDao.fetchAvailabilities(tutor, startDate, endDate);

        try (var session = openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    List<TimeRange> timeRanges = weeklyTimeRanges.getOrDefault(dayOfWeek, List.of());
                    for (TimeRange timeRange : timeRanges) {
                        createNewAvailabilityFromTimeRange(tutor, timeRange, date, existingAvailabilities, session);
                    }
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    private void createNewAvailabilityFromTimeRange(Tutor tutor, TimeRange timeRange, LocalDate date,
                                                    List<Availability> existingAvailabilities,
                                                    Session session) {
        if (timeRange.start() != null && timeRange.end() != null) {
            LocalDateTime startDateTime = LocalDateTime.of(date, timeRange.start());
            LocalDateTime endDateTime = LocalDateTime.of(date, timeRange.end());

            if (hasConflict(existingAvailabilities, startDateTime, endDateTime)) {
                throw new ValidationException("Tutor already has an availability that conflicts with the new one");
            }

            Availability availability = new Availability(tutor, startDateTime, endDateTime);
            availabilityDao.save(availability, session);
        }
    }

    private boolean hasConflict(List<Availability> existingAvailabilities, LocalDateTime newStart, LocalDateTime
            newEnd) {
        for (Availability availability : existingAvailabilities) {
            if (availability.getStartDateTime().isBefore(newEnd) && availability.getEndDateTime().isAfter(newStart)) {
                return true;
            }
        }
        return false;
    }


    public void addOneTimeAvailability(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new ValidationException("Start time must be before end time");
        }

        checkIfTutorHasConflictingAvailability(tutor, start, end);
        var availability = new Availability(tutor, start, end);
        validateEntity(availability);
        availabilityDao.save(availability);
    }

    private void checkIfTutorHasConflictingAvailability(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        try (var session = openSession()) {
            var query = session.createQuery(
                    "SELECT COUNT(*) FROM Availability a " +
                            "WHERE a.tutor = :tutor AND " +
                            "a.startDateTime <= :end AND a.endDateTime >= :start",
                    Long.class
            );

            query.setParameter("tutor", tutor);
            query.setParameter("start", start);
            query.setParameter("end", end);

            if (query.getSingleResult() > 0) {
                throw new ValidationException("Tutor already has an availability that conflicts with this one");
            }
        }
    }


    @Override
    protected void saveUser(Tutor user) {
        tutorDao.save(user);
    }
}
