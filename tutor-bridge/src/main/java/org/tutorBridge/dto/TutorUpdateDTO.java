package org.tutorBridge.dto;

import jakarta.validation.constraints.Past;
import org.tutorBridge.validation.ValidPhoneNumber;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

public class TutorUpdateDTO implements Serializable {
    private String firstName;
    private String lastName;
    @ValidPhoneNumber
    private String phone;
    @Past(message = "Birthdate must be in the past")
    private LocalDate birthDate;
    private Set<String> specializations;
    private String bio;

    public TutorUpdateDTO() {
    }

    public TutorUpdateDTO(String firstName, String lastName, String phone, LocalDate birthDate, Set<String> specializations, String bio) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.birthDate = birthDate;
        this.specializations = specializations;
        this.bio = bio;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Set<String> getSpecializations() {
        return specializations;
    }


    public String getBio() {
        return bio;
    }

}

