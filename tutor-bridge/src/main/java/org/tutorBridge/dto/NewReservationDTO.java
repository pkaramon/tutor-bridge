package org.tutorBridge.dto;


import jakarta.validation.constraints.NotNull;

public class NewReservationDTO {
    private Long reservationId;
    @NotNull(message = "Availability ID is required")
    private Long availabilityId;

    @NotNull(message = "Specialization ID is required")
    private Long specializationId;

    public Long getAvailabilityId() {
        return availabilityId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Long getSpecializationId() {
        return specializationId;
    }
}
