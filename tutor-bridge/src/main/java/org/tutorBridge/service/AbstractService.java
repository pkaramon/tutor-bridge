package org.tutorBridge.service;

import org.hibernate.Session;
import org.tutorBridge.config.HibernateUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.stream.Collectors;

public abstract class AbstractService {

    private final Validator validator;

    protected AbstractService() {
        var factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    protected Validator getValidator() {
        return validator;
    }

    protected void validateEntity(Object entity) {
        var violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new ValidationException(
                    violations
                            .stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining()));
        }
    }

    protected Session openSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }
}
