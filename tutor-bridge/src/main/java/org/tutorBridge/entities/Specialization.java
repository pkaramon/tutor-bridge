package org.tutorBridge.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SPECIALIZATION")
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "specialization_seq")
    @SequenceGenerator(name = "specialization_seq", sequenceName = "BD_415271.SPECIALIZATION_ID_SEQ", allocationSize = 1)
    @Column(name = "SPECIALIZATIONID", nullable = false)
    private Long specializationId;

    @Column(name = "NAME", nullable = false, length = 200)
    private String name;


    @ManyToMany(mappedBy = "specializations")
    private Set<Tutor> tutors = new HashSet<>();


    public Specialization() {

    }

    public Specialization(String name) {
        this.name = name;
    }


    public Long getSpecializationId() {
        return specializationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Tutor> getTutors() {
        return tutors;
    }
}
