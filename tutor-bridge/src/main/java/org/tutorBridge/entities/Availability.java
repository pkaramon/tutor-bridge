package org.tutorBridge.entities;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
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

    @Column(name = "\"DATE\"", nullable = false)
    private LocalDate date;

    @Column(name = "STARTHOUR", nullable = false)
    private int startHour;

    @Column(name = "STARTMINUTE", nullable = false)
    private int startMinute;


    @Column(name = "ENDHOUR", nullable = false)
    private int endHour;
    @Column(name = "ENDMINUTE", nullable = false)
    private int endMinute;

    public Availability() {
    }

    public Availability(Tutor tutor, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.tutor = tutor;
        this.date = date;
        this.setStartTime(startTime);
        this.setEndTime(endTime);
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

    public LocalDate getDate() {
        return date;
    }

    public void setStartTime() {

    }

    public void setStartTime(LocalTime time) {
        this.startHour = time.getHour();
        this.startMinute = time.getMinute();
    }

    public void setEndTime(LocalTime time) {
        this.endHour = time.getHour();
        this.endMinute = time.getMinute();
    }

    public LocalTime getStartTime() {
        return LocalTime.of(startHour, startMinute);
    }
    public LocalTime getEndTime() {
        return LocalTime.of(endHour, endMinute);
    }
}

