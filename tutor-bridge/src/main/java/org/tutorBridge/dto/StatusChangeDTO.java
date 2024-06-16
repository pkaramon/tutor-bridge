package org.tutorBridge.dto;

import jakarta.validation.constraints.NotNull;
import org.tutorBridge.entities.enums.ReservationStatus;

public class StatusChangeDTO {
    @NotNull(message = "Reservation ID is required")
    private Long reservationId;
    @NotNull(message = "Status is required")
    private ReservationStatus status;


    public StatusChangeDTO() {
    }

    public Long getReservationId() {
        return reservationId;
    }


    public ReservationStatus getStatus() {
        return status;
    }

}
