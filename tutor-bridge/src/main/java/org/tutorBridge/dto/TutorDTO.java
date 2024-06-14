package org.tutorBridge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.tutorBridge.entities.enums.UserType;
import org.tutorBridge.validation.ValidPhoneNumber;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link org.tutorBridge.entities.Tutor}
 */
public class TutorDTO implements Serializable {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Phone number is required")
    @ValidPhoneNumber
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
    @NotNull(message = "Birthdate is required")
    @Past(message = "Birthdate must be in the past")
    private LocalDate birthDate;
    private Set<SpecializationDto> specializations;
    private String bio;
    private Long userId;

    private final UserType type = UserType.TUTOR;

    public TutorDTO(String firstName, String lastName, String phone, String email, String password, LocalDate birthDate, Set<SpecializationDto> specializations, String bio,
                    Long userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.specializations = specializations;
        this.bio = bio;
        this.userId = userId;
    }

    public TutorDTO() {
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

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getType() {
        return type;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Set<SpecializationDto> getSpecializations() {
        return specializations;
    }

    public String getBio() {
        return bio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TutorDTO entity = (TutorDTO) o;
        return Objects.equals(this.firstName, entity.firstName) &&
                Objects.equals(this.lastName, entity.lastName) &&
                Objects.equals(this.phone, entity.phone) &&
                Objects.equals(this.email, entity.email) &&
                Objects.equals(this.password, entity.password) &&
                Objects.equals(this.type, entity.type) &&
                Objects.equals(this.birthDate, entity.birthDate) &&
                Objects.equals(this.specializations, entity.specializations) &&
                Objects.equals(this.bio, entity.bio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, phone, email, password, type, birthDate, specializations, bio);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "firstName = " + firstName + ", " +
                "lastName = " + lastName + ", " +
                "phone = " + phone + ", " +
                "email = " + email + ", " +
                "password = " + password + ", " +
                "type = " + type + ", " +
                "birthDate = " + birthDate + ", " +
                "specializations = " + specializations + ", " +
                "bio = " + bio + ")";
    }

    public Long getUserId() {
        return userId;
    }
}