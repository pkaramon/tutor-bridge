package org.tutorBridge.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.tutorBridge.config.HibernateUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
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

    public void save(T entity) {
        validateEntity(entity);
        Transaction transaction = null;
        try (Session session = openSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    public void save(T entity, Session session) {
        validateEntity(entity);
        session.save(entity);
    }

    public void update(T entity, Session session) {
        validateEntity(entity);
        session.update(entity);
    }

    public Optional<T> findById(ID id) {
        try (Session session = openSession()) {
            return Optional.ofNullable(session.get(entityClass, id));
        }
    }

    public List<T> findAll() {
        try (Session session = openSession()) {
            return session.createQuery("from " + entityClass.getName(), entityClass).list();
        }
    }

    public void update(T entity) {
        validateEntity(entity);
        Transaction transaction = null;
        try (Session session = openSession()) {
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void delete(T entity) {
        Transaction transaction = null;
        try (Session session = openSession()) {
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    protected Session openSession() {
        return HibernateUtil.getSessionFactory().openSession();
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
}
