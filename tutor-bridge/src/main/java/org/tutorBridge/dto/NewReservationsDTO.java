package org.tutorBridge.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class NewReservationsDTO {
    @NotNull(message = "Reservations are required")
    private List<@Valid NewReservationDTO> reservations;

    public NewReservationsDTO() {
    }

    public List<NewReservationDTO> getReservations() {
        return reservations;
    }
}
