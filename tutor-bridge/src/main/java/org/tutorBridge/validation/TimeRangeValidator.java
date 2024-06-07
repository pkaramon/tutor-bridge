package org.tutorBridge.validation;

import org.tutorBridge.entities.Availability;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimeRangeValidator implements ConstraintValidator<ValidTimeRange, Availability> {
    @Override
    public boolean isValid(Availability availability, ConstraintValidatorContext constraintValidatorContext) {
        if(availability == null) {
            return false;
        }
        if(availability.getStartTime() == null || availability.getEndTime() == null) {
            return false;
        }
        return availability.getStartTime().isBefore(availability.getEndTime());
    }
}
