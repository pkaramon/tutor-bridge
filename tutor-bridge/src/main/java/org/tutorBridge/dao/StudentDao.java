package org.tutorBridge.dao;

import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.tutorBridge.entities.Student;

import java.util.Optional;


@Repository
public class StudentDao extends GenericDao<Student, Long> {
    public StudentDao() {
        super(Student.class);
    }

    public Optional<Student> findByEmail(String email) {
        TypedQuery<Student> query = em.createQuery("from Student where email = :email", Student.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }
}
