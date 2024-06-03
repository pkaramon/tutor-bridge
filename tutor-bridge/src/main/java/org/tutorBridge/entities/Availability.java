package org.tutorBridge.entities;


import javax.persistence.*;
import java.time.LocalDate;

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

    @Column(name = "ENDHOUR", nullable = false)
    private int endHour;

    @Column(name = "STARTMINUTE")
    private Integer startMinute;

    @Column(name = "ENDMINUTE")
    private Integer endMinute;

    public Availability() {
    }



    public Long getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(Long availabilityId) {
        this.availabilityId = availabilityId;
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

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public Integer getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(Integer startMinute) {
        this.startMinute = startMinute;
    }

    public Integer getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }
}

