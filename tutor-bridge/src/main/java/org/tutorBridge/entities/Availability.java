package org.tutorBridge.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.tutorBridge.validation.ValidAvailabilityRange;

import java.time.LocalDateTime;

@Entity
@ValidAvailabilityRange
@Table(name = "AVAILABILITY")
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "availability_seq")
    @SequenceGenerator(name = "availability_seq", sequenceName = "BD_415271.AVAILABILITY_ID_SEQ", allocationSize = 1)
    @Column(name = "AVAILABILITYID", nullable = false)
    private Long availabilityId;

    @ManyToOne
    @JoinColumn(name = "TUTORID", nullable = false)
    private Tutor tutor;

    @NotNull(message = "Start date is required")
    @Column(name = "STARTDATE", nullable = false)
    private LocalDateTime startDateTime;


    @NotNull(message = "End date is required")
    @Column(name = "ENDDATE", nullable = false)
    private LocalDateTime endDateTime;


    public Availability() {
    }

    public Availability(Tutor tutor, LocalDateTime start, LocalDateTime end) {
        this.tutor = tutor;
        this.startDateTime = start;
        this.endDateTime = end;
    }

    public Long getAvailabilityId() {
        return availabilityId;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
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
}

