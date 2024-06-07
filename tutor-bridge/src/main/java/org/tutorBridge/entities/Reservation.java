package org.tutorBridge.entities;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "RESERVATION")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_seq")
    @SequenceGenerator(name = "reservation_seq", sequenceName = "BD_415271.RESERVATION_ID_SEQ", allocationSize = 1)
    @Column(name = "RESERVATIONID", nullable = false)
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "STUDENTID", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "TUTORID", nullable = false)
    private Tutor tutor;

    @ManyToOne
    @JoinColumn(name = "SPECIALIZATIONID", nullable = false)
    private Specialization specialization;

    @Positive
    @Column(name = "STARTHOUR", nullable = false)
    private int startHour;

    @Positive
    @Column(name = "ENDHOUR", nullable = false)
    private int endHour;

    @Column(name = "STARTMINUTE", nullable = false)
    private int startMinute;

    @Column(name = "ENDMINUTE", nullable = false)
    private int endMinute;

    @Column(name = "\"DATE\"", nullable = false)
    private LocalDate date;

    @Convert(converter = ReservationStatusConverter.class)
    @Column(name = "STATUS", length = 1, nullable = false)
    private ReservationStatus status;


    public Reservation() {
    }

    public Reservation(Student student, Tutor tutor, Specialization specialization, LocalTime startTime, LocalTime endTime, LocalDate date) {
        this.student = student;
        this.tutor = tutor;
        this.specialization = specialization;
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.date = date;
        this.status = ReservationStatus.NEW;
    }


    public Long getReservationId() {
        return reservationId;
    }


    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }


    public Specialization getSpecialization() {
        return specialization;
    }

    public int getStartHour() {
        return startHour;
    }

    public LocalTime getStartTime() {
        return LocalTime.of(startHour, startMinute);
    }

    public void setStartTime(LocalTime time) {
        this.startHour = time.getHour();
        this.startMinute = time.getMinute();
    }

    public LocalTime getEndTime() {
        return LocalTime.of(endHour, endMinute);
    }

    public void setEndTime(LocalTime time) {
        this.endHour = time.getHour();
        this.endMinute = time.getMinute();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
