package org.tutorBridge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import org.tutorBridge.entities.enums.StudentLevel;
import org.tutorBridge.validation.ValidPhoneNumber;

import java.io.Serializable;
import java.time.LocalDate;

public class StudentUpdateDTO implements Serializable {
    private String firstName;
    private String lastName;
    @ValidPhoneNumber
    private String phone;
    @Email(message = "Email must be valid")
    private String email;
    @Past(message = "Birthdate must be in the past")
    private LocalDate birthDate;
    private StudentLevel level;

    // Getters and Setters

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
