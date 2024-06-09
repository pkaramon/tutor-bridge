package org.tutorBridge.dao;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.tutorBridge.config.DB;
import org.tutorBridge.validation.ValidationException;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GenericDao<T, ID extends Serializable> {

    private final Validator validator;
    private final Class<T> entityClass;

    public GenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;
        var factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public void save(T entity, EntityManager entityManager) {
        validateEntity(entity);
        entityManager.persist(entity);
    }

    public void save(T entity) {
        validateEntity(entity);
        DB.inTransaction(em -> em.persist(entity));
    }

    public Optional<T> findById(ID id, EntityManager em) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    public List<T> findAll(EntityManager em) {
        return em.createQuery("from " + entityClass.getName(), entityClass).getResultList();
    }

    public void update(T entity, EntityManager entityManager) {
        validateEntity(entity);
        entityManager.merge(entity);
    }

    public void update(T entity) {
        validateEntity(entity);
        DB.inTransaction(em -> em.merge(entity));
    }

    public void delete(T entity, EntityManager entityManager) {
        entityManager.remove(entity);
    }

    public void delete(T entity) {
        DB.inTransaction(em -> em.remove(entity));
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
