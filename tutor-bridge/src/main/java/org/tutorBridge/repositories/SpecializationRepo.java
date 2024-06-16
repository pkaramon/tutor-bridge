package org.tutorBridge.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.tutorBridge.entities.Specialization;
import org.tutorBridge.entities.Tutor;

import java.util.List;
import java.util.Optional;


@Repository
public class SpecializationRepo extends GenericRepo<Specialization, Long> {
    @PersistenceContext
    private EntityManager em;

    public SpecializationRepo() {
        super(Specialization.class);
    }

    public Optional<Specialization> findByName(String name) {
        return em.createQuery("SELECT s FROM Specialization s WHERE lower(s.name) = lower(:name)", Specialization.class)
                .setParameter("name", name.toLowerCase())
                .getResultList()
                .stream()
                .findFirst();
    }

    public List<Specialization> findFor(Tutor tutor) {
        return em.createQuery("SELECT s FROM Specialization s " +
                        "JOIN s.tutors t WHERE t = :tutor", Specialization.class)
                .setParameter("tutor", tutor)
                .getResultList();

    }
}
