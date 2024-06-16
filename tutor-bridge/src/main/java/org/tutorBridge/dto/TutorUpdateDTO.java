package org.tutorBridge.dto;

import jakarta.validation.constraints.Email;
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
    @Email(message = "Email must be valid")
    private String email;
    @Past(message = "Birthdate must be in the past")
    private LocalDate birthDate;
    private Set<String> specializations;
    private String bio;


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

    public Set<String> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(Set<String> specializations) {
        this.specializations = specializations;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}

