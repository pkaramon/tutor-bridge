package org.tutorBridge.dao;

import jakarta.persistence.EntityManager;
import org.tutorBridge.entities.Specialization;

import java.util.Optional;


public class SpecializationDao extends GenericDao<Specialization, Long> {
    public SpecializationDao() {
        super(Specialization.class);
    }

    public Optional<Specialization> findByName(String name, EntityManager em) {
        return em.createQuery("SELECT s FROM Specialization s WHERE s.name = :name", Specialization.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
    }
}
