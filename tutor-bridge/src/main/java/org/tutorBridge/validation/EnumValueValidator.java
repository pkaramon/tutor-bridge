package org.tutorBridge.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValueValidator implements ConstraintValidator<EnumValue, String> {
    private Enum<?>[] enumConstants;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        enumConstants = constraintAnnotation.enumClass().getEnumConstants();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return Arrays.stream(enumConstants)
                .anyMatch(enumConstant -> enumConstant.name().equals(value));
    }
}
