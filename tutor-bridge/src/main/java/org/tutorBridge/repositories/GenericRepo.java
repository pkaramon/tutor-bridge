package org.tutorBridge.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.tutorBridge.validation.ValidationException;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GenericRepo<T, ID extends Serializable> {

    private final Validator validator;
    private final Class<T> entityClass;

    @PersistenceContext
    protected EntityManager em;

    public GenericRepo(Class<T> entityClass) {
        this.entityClass = entityClass;
        var factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public void save(T entity) {
        validateEntity(entity);
        em.persist(entity);
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    public List<T> findAll() {
        return em.createQuery("from " + entityClass.getName(), entityClass).getResultList();
    }

    public void update(T entity) {
        validateEntity(entity);
        em.merge(entity);
    }


    public void delete(T entity) {
        em.remove(entity);
    }

    protected void validateEntity(Object entity) {
        var violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining()));
        }
    }

}
