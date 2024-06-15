package org.tutorBridge.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link org.tutorBridge.entities.Specialization}
 */
public class SpecializationDto implements Serializable {
    private Long specializationId;
    @NotBlank(message = "Specialization name is required")
    private String name;

    public SpecializationDto(Long specializationId, String name) {
        this.specializationId = specializationId;
        this.name = name;
    }

    public SpecializationDto() {}

    public Long getSpecializationId() {
        return specializationId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecializationDto entity = (SpecializationDto) o;
        return Objects.equals(this.specializationId, entity.specializationId) &&
                Objects.equals(this.name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specializationId, name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "specializationId = " + specializationId + ", " +
                "name = " + name + ")";
    }
}