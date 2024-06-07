package org.tutorBridge.validation;

import org.tutorBridge.entities.Absence;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Absence> {

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(Absence absence, ConstraintValidatorContext context) {
        if (absence == null) {
            return false;
        }
        if (absence.getStartDate() == null || absence.getEndDate() == null) {
            return false;
        }
        return absence.getStartDate().isBefore(absence.getEndDate());
    }
}
