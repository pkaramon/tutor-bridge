package org.tutorBridge.entities;


import org.tutorBridge.validation.ValidDateRange;

import java.time.LocalDate;
import javax.persistence.*;

@Entity
@ValidDateRange
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
    private LocalDate startDate;

    @Column(name = "ENDDATE", nullable = false)
    private LocalDate endDate;

    public Absence() {
    }

    public Absence(Tutor tutor, LocalDate startDate, LocalDate endDate) {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

}
