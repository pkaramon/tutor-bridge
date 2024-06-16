package org.tutorBridge.dto;


import org.tutorBridge.entities.Availability;

import java.time.LocalDateTime;

public class AvailabilityDTO {
    private Long availabilityId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public AvailabilityDTO(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public AvailabilityDTO(Long availabilityId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.availabilityId = availabilityId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public static AvailabilityDTO fromAvailability(Availability availability) {
        return new AvailabilityDTO(availability.getStartDateTime(), availability.getEndDateTime());
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Long getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(Long availabilityId) {
        this.availabilityId = availabilityId;
    }
}
