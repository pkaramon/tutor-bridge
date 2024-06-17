package org.tutorBridge.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tutorBridge.dto.AvailabilityDTO;
import org.tutorBridge.dto.TimeFrameDTO;
import org.tutorBridge.dto.TimeRangeDTO;
import org.tutorBridge.dto.WeeklySlotsDTO;
import org.tutorBridge.entities.Availability;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.repositories.AvailabilityRepo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {
    private final AvailabilityRepo availabilityRepo;

    public AvailabilityService(AvailabilityRepo availabilityRepo) {
        this.availabilityRepo = availabilityRepo;
    }


    public List<AvailabilityDTO> getAvailabilities(Tutor tutor, TimeFrameDTO timeFrame) {
        return fromAvailabilitiesToDTOS(
                availabilityRepo.fetchOverlapping(tutor, timeFrame.getStart(), timeFrame.getEnd())
        );
    }


    @Transactional
    public List<AvailabilityDTO> addWeeklyAvailability(Tutor tutor, WeeklySlotsDTO slots) {
        LocalDate startDate = slots.getStartDate();
        LocalDate endDate = slots.getEndDate();
        Map<DayOfWeek, List<TimeRangeDTO>> weeklyTimeRanges = slots.getWeeklyTimeRanges();

        clearTimeFrameFromPreviousAvailabilities(tutor, startDate, endDate);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            Set<TimeRangeDTO> timeRanges = Set.copyOf(weeklyTimeRanges.getOrDefault(dayOfWeek, List.of()));
            for (var timeRange : timeRanges) {
                createNewAvailabilityFromTimeRange(tutor, timeRange, date);
            }
        }

        return fromAvailabilitiesToDTOS(availabilityRepo.fetchOverlapping(
                tutor,
                slots.getStartDate().atStartOfDay(),
                slots.getEndDate().plusDays(1).atStartOfDay()));
    }

    private void clearTimeFrameFromPreviousAvailabilities(Tutor tutor, LocalDate startDate, LocalDate endDate) {
        availabilityRepo.deleteAllOverlapping(
                tutor,
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay());
    }


    private void createNewAvailabilityFromTimeRange(Tutor tutor, TimeRangeDTO timeRange, LocalDate date) {
        if (timeRange.getStart() != null && timeRange.getEnd() != null) {
            LocalDateTime startDateTime = LocalDateTime.of(date, timeRange.getStart());
            LocalDateTime endDateTime = LocalDateTime.of(date, timeRange.getEnd());
            Availability availability = new Availability(tutor, startDateTime, endDateTime);
            availabilityRepo.save(availability);
        }
    }

    private List<AvailabilityDTO> fromAvailabilitiesToDTOS(List<Availability> availabilities) {
        return availabilities.stream()
                .map(a -> new AvailabilityDTO(a.getAvailabilityId(), a.getStartDateTime(), a.getEndDateTime()))
                .collect(Collectors.toList());
    }


}
