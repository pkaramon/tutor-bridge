package org.tutorBridge.entities;


import org.tutorBridge.validation.ValidAbsenceRange;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@ValidAbsenceRange
@Table(name = "ABSENCE")
public class Absence {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "absence_seq")
    @SequenceGenerator(name = "absence_seq", sequenceName = "BD_415271.ABSENCE_ID_SEQ", allocationSize = 1)
    @Column(name = "ABSENCEID", nullable = false)
    private Long absenceId;

    @ManyToOne
    @JoinColumn(name = "TUTORID", nullable = false)
    private Tutor tutor;

    @Column(name = "STARTDATE", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "ENDDATE", nullable = false)
    private LocalDateTime endDate;

    public Absence() {
    }

    public Absence(Tutor tutor, LocalDateTime startDate, LocalDateTime endDate) {
        this.tutor = tutor;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getAbsenceId() {
        return absenceId;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

}
