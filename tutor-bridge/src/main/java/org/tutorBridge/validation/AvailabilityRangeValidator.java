package org.tutorBridge.validation;

import org.tutorBridge.entities.Availability;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AvailabilityRangeValidator implements ConstraintValidator<ValidAvailabilityRange, Availability> {
    @Override
    public boolean isValid(Availability availability, ConstraintValidatorContext constraintValidatorContext) {
        if (availability == null) {
            return false;
        }
        return availability.getStartDateTime().isBefore(availability.getEndDateTime());
    }
}
