package org.tutorBridge.dto;

import jakarta.validation.constraints.Past;
import org.tutorBridge.entities.enums.StudentLevel;
import org.tutorBridge.validation.PhoneNumber;

import java.io.Serializable;
import java.time.LocalDate;

public class StudentUpdateDTO implements Serializable {
    private String firstName;
    private String lastName;
    @PhoneNumber
    private String phone;
    @Past(message = "Birthdate must be in the past")
    private LocalDate birthDate;
    private StudentLevel level;


    public StudentUpdateDTO(String firstName, String lastName, String phone, LocalDate birthDate, StudentLevel level) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.birthDate = birthDate;
        this.level = level;
    }

    public StudentUpdateDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public StudentLevel getLevel() {
        return level;
    }

    public void setLevel(StudentLevel level) {
        this.level = level;
    }
}
