package org.tutorBridge.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.tutorBridge.entities.Specialization;

import java.util.Optional;


@Repository
public class SpecializationDao extends GenericDao<Specialization, Long> {
    @PersistenceContext
    private EntityManager em;

    public SpecializationDao() {
        super(Specialization.class);
    }

    public Optional<Specialization> findByName(String name) {
        return em.createQuery("SELECT s FROM Specialization s WHERE s.name = :name", Specialization.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
    }
}
